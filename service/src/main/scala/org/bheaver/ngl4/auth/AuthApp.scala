package org.bheaver.ngl4.auth

import akka.http.scaladsl.server.{Directive, HttpApp, Route}
import org.bheaver.ngl4.auth.authentication.AuthenticationServiceImpl
import org.bheaver.ngl4.auth.authentication.model._
import akka.http.scaladsl.settings.ServerSettings
import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import spray.json.DefaultJsonProtocol._

class AuthApp {
  val settings = ServerSettings(ConfigFactory.load).withDefaultHttpPort(80).withVerboseErrorMessages(true)
  def start = WebApp.startServer("localhost", 8080, settings)
}
object AuthApp extends App {
  val injector = Guice.createInjector(new AuthModule)

  injector.getInstance(classOf[AuthApp]).start
}

object WebApp extends HttpApp {
  implicit val itemFormat = jsonFormat2(AuthenticateResponse)
  val authService = new AuthenticationServiceImpl

  override protected def routes: Route =
    concat {
      path("auth") {
        get { ctx =>
          val headers = ctx.request.headers
          headers.foreach(header => {
            println(header)
          })

          ctx.complete {
            "Received auth with libId "
          }
        }
      }
    }

}

