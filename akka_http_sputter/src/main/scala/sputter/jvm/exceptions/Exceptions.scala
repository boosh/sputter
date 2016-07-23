package sputter.exceptions

/**
  * Base class for all exceptions in this library
  */
trait AkkaHttpExtensionsException {
  val message: String
}

/**
  * Thrown when there is a problem persisting data to a datastore
  */
case class DataStoreException(message: String)
  extends Exception(message: String) with AkkaHttpExtensionsException

/**
  * Thrown when validation on an object fails
  * @param errors
  */
case class ValidationException(message: String, errors: List[String])
  extends Exception(message: String) with AkkaHttpExtensionsException
