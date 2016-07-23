package sputter.jvm.auth.providers

import sputter.jvm.auth.Credentials

import scala.concurrent.Future

/**
  * Authenticate users with Facebook
  */
object FacebookProvider extends AuthProvider {

  override def authenticate(credentials: Credentials): Future[Boolean] = ???
}
