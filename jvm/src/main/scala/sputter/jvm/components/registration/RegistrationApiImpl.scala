package sputter.jvm.components.registration

import akka.event.LoggingAdapter
import sputter.jvm.components.exceptions.ValidationException
import sputter.jvm.components.registration.datastore.RegistrationService
import sputter.shared._

/**
  * Handles user registration
  */
trait RegistrationApiImpl extends RegistrationApi {

  val registrationService: RegistrationService

  val logger: LoggingAdapter

  override def register(form: RegistrationForm): StandardResponse = {
    logger.debug(s"Received registration form POST request")

    registrationService.handleForm(form = form) match {
      // todo: wrap this in `complete`
      case Right(_) => StandardResponse(status = ResponseOK(), errors = List())
      // todo: wrap this in `reject`
      case Left(e: ValidationException) => StandardResponse(
        status = ResponseError(), errors = e.errors)
      //        reject(ValidationListRejection(message = e.message, errors = e.errors))
      case Left(e) => throw new RuntimeException(e.message)
    }
  }
}
