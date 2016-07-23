package sputter.jvm.auth.providers

import sputter.jvm.auth.Credentials

import scala.concurrent.Future

/**
  * Trait all auth providers must implement
  */
trait AuthProvider {

  def authenticate(credentials: Credentials): Future[Boolean]
}
