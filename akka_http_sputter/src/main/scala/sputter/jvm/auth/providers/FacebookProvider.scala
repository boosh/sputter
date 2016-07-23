package sputter.auth.providers

import sputter.auth.Credentials

import scala.concurrent.Future

/**
  * Authenticate users with Facebook
  */
object FacebookProvider extends AuthProvider {

  override def authenticate(credentials: Credentials): Future[Boolean] = ???
}
