package org.bheaver.ngl4.auth

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import org.bheaver.ngl4.auth.authentication.AuthenticationServiceImpl
import spray.json.DefaultJsonProtocol._
import org.bheaver.ngl4.auth.authentication.model._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.concurrent.Future
import scala.io.StdIn
import org.bheaver.ngl4.base.model
import org.bheaver.ngl4.base.model.LibCode
import org.bheaver.ngl4.base.constants.HttpHeaderNames._

object AuthApp extends App {
  implicit val actorSystem = ActorSystem("authenticationAuthorizationApp")

  implicit val executionContext = actorSystem.dispatcher

  implicit val respFormat = jsonFormat7(AuthenticateResponse)

  val authenticationService = new AuthenticationServiceImpl

  val route = {

      path("auth") {
        post {
          headerValueByName(X_LIB_CODE.toString){
            libId => {
              println(s"libId ${libId}")
              implicit val libCode = LibCode(libId)

              formFields("patronId".as[String], "username".as[String], "password".as[String]){
                (patronId, username, password) => {
                  val authenticateRequest = AuthenticateRequest(patronId, username, password, null)

                  val eventualResponse = authenticationService.authenticate(authenticateRequest)

                  onSuccess(eventualResponse) {
                    item => {
                      complete(item)
                    }
                  }
                }
              }
            }
          }
        }
      }


  }

  private val eventualBinding: Future[Http.ServerBinding] = Http().newServerAt("localhost", 8080).bind(route)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()

  eventualBinding
    .flatMap(_.unbind())
    .onComplete(_ => actorSystem.terminate())
}