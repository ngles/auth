package org.bheaver.ngl4.auth.authentication

package object model {

  case class AuthenticateRequest(patronId: String, userName: String, password: String, hashedPassword: String)

  case class AuthenticateResponse(authenticated: Boolean, fname: String = null, mname: String = null, lname: String = null, departmentId: String = null, patronCategoryId: String = null, jwtToken: String = null)

  /*object AuthenticateResponse {
    def apply(authenticated: Boolean) = new AuthenticateResponse(authenticated)
  }*/

  case class JWTRenewRequest(jwt: String)

  case class JWTRenewResponse(jwt: String)

}
