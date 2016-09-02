package sputter.jvm.components.contactform

import akka.event.LoggingAdapter
import sputter.jvm.components.contactform.datastore.ContactFormService
import sputter.jvm.components.exceptions.ValidationException
import sputter.shared.{ResponseError, ResponseOK}
import sputter.shared.contactform.{ContactForm, ContactFormApi, ContactFormResponse}


/**
  * Endpoint to let users contact you. You can optionally be notified when
  * they do.
  */
trait ContactFormApiImpl extends ContactFormApi {

  val contactFormService: ContactFormService

  val logger: LoggingAdapter

  override def contact(form: ContactForm) = {

    logger.debug(s"Received contact form POST request with payload: $form")

    contactFormService.handleForm(contactForm = form) match {
        // todo: wrap this in `complete`
      case Right(_) => ContactFormResponse(status = ResponseOK(), errors = List())
      // todo: wrap this in `reject`
      case Left(e: ValidationException) => ContactFormResponse(
        status = ResponseError(), errors = e.errors)
//        reject(ValidationListRejection(message = e.message, errors = e.errors))
      case Left(e) => throw new RuntimeException(e.message)
    }
  }
}
