package org.bheaver.ngl4.auth.authentication

import org.bheaver.ngl4.businessmodel.Patron

package object model {
  case class AuthenticateRequest(libCode: String, patronId: String, userName: String, password: String)

  case class AuthenticateResponse(authentic: Boolean, patron: String)
}
