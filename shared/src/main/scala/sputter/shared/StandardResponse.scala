package sputter.shared

// Returned by several APIs
case class StandardResponse(status: ResponseStatus, errors: List[String])

