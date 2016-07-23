package sputter.jvm.components.auth.providers

import sputter.jvm.components.auth.Credentials

import scala.concurrent.Future

/**
  * Trait all auth providers must implement
  */
trait AuthProvider {

  def authenticate(credentials: Credentials): Future[Boolean]
}
