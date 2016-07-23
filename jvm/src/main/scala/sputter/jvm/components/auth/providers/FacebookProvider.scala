package sputter.jvm.components.auth.providers

import sputter.jvm.components.auth.Credentials

import scala.concurrent.Future

/**
  * Authenticate users with Facebook
  */
object FacebookProvider extends AuthProvider {

  override def authenticate(credentials: Credentials): Future[Boolean] = ???
}
