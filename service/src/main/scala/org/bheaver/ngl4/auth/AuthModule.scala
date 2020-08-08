package org.bheaver.ngl4.auth

import com.google.inject.{AbstractModule, Provides}
import org.bheaver.ngl4.auth.authentication.{AuthenticationService, AuthenticationServiceImpl}

class AuthModule extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[AuthenticationService]).to(classOf[AuthenticationServiceImpl])
  }
}
