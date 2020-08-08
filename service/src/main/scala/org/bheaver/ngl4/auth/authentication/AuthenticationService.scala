package org.bheaver.ngl4.auth.authentication

import org.bheaver.ngl4.auth.authentication.model.{AuthenticateRequest, AuthenticateResponse}
import org.bheaver.ngl4.businessmodel.Patron

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global;

trait AuthenticationService {

  def authenticate(authenticateRequest: AuthenticateRequest): Future[AuthenticateResponse]

}

class AuthenticationServiceImpl extends AuthenticationService {

  override def authenticate(authenticateRequest: AuthenticateRequest): Future[AuthenticateResponse] = Future(AuthenticateResponse(true, "Myname"))

}