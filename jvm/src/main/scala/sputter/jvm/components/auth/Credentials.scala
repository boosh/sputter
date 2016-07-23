package sputter.jvm.components.auth

/**
  * Encapsulates credentials submitted by a user to log in with.
  */
case class Credentials(secret: String, id: Option[String], provider: String)
