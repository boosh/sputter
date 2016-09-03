package sputter.jvm.components.contact

import akka.event.LoggingAdapter
import sputter.jvm.components.contact.datastore.ContactService
import sputter.jvm.components.exceptions.ValidationException
import sputter.shared._


/**
  * Endpoint to let users contact you. You can optionally be notified when
  * they do.
  */
trait ContactApiImpl extends ContactApi {

  val contactService: ContactService

  val logger: LoggingAdapter

  override def contact(form: ContactForm) = {

    logger.debug(s"Received contact form POST request with payload: $form")

    contactService.handleForm(form = form) match {
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
