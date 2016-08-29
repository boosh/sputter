package sputter.shared

/**
  * Constants of possible response statuses
  */
sealed trait ResponseStatus

case class ResponseOK() extends ResponseStatus {
  val value = "OK"
}

case class ResponseError() extends ResponseStatus {
  val value = "ERROR"
}
