package org.bheaver.ngl4.auth.authentication

import com.mongodb.client.model.Filters
import org.bheaver.ngl4.auth.authentication.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.bheaver.ngl4.base.persistence.MongodbConnection._
import com.mongodb.client.model.Filters._
import org.apache.commons.codec.digest.DigestUtils
import org.mongodb.scala.bson.collection.immutable.Document
import org.bheaver.ngl4.base.util.db.DocumentUtils._
import org.mongodb.scala.Observable
import org.bheaver.ngl4.base.model.LibCode

trait AuthenticationService {
  def authenticate(authenticateRequest: AuthenticateRequest)(implicit libCode: LibCode): Future[AuthenticateResponse]
}

class AuthenticationServiceImpl extends AuthenticationService {

  val updateWithHashedPassword: AuthenticateRequest => AuthenticateRequest = authReq => authReq.copy(hashedPassword = DigestUtils.md5Hex(authReq.password))

  val fillLibCode: LibCode => Future[Option[LibCode]] = libCode => libCode.fillLibCode()

  val queryDB: (AuthenticateRequest, LibCode) => Observable[Document] = (authReq, libCode) => libCode.getDatabase().getCollection("patron").find(
    and(Filters.eq("patron_id", authReq.patronId), Filters.eq("user_password", authReq.hashedPassword))
  ).projection(Document("fname" -> 1, "mname" -> 1, "lname" -> 1, "patron_category_id" -> 1, "dept_id" -> 1))

  val convertMongoDocToAuthResponse: Observable[Document] => Observable[AuthenticateResponse] = doc => doc.map(document => AuthenticateResponse(true,
    document("fname"),
    document("mname"),
    document("lname"),
    document("patron_category_id"),
    document("dept_id"),
    ""))

  val createAuthResponse: Future[Seq[AuthenticateResponse]] => Future[AuthenticateResponse] = seqAuthRes => seqAuthRes.map(seq => seq.find(a => a.authenticated).getOrElse(AuthenticateResponse(false, "", "", "", "", "", "")))

  override def authenticate(authenticateRequest: AuthenticateRequest)(implicit libCode: LibCode): Future[AuthenticateResponse] = {
    fillLibCode(libCode).flatMap {
      case Some(filledLibCode) => updateWithHashedPassword
        .andThen(authReq => queryDB(authReq, filledLibCode))
        .andThen(convertMongoDocToAuthResponse)
        .andThen(res => res.toFuture())
        .andThen(createAuthResponse)(authenticateRequest)

      case None => Future(AuthenticateResponse(authenticated = false))
    }
  }

}