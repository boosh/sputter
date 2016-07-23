package sputter.auth.providers

import sputter.auth.Credentials

import scala.concurrent.Future

/**
  * Trait all auth providers must implement
  */
trait AuthProvider {

  def authenticate(credentials: Credentials): Future[Boolean]
}
