package sputter.jvm.components.akkahttp

import akka.http.javadsl.{server => jserver}
import akka.http.scaladsl.server.RejectionWithOptionalCause

/**
  * Rejection that allows a list of rejection messages
  *
  * @param message Error message
  * @param errors List of messages
  * @param cause
  */
case class ValidationListRejection(message: String, errors: List[String], cause: Option[Throwable] = None)
  extends jserver.ValidationRejection with RejectionWithOptionalCause
