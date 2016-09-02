package sputter.jvm.components.contactform

import akka.event.LoggingAdapter
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import sputter.jvm.components.akkahttp.ValidationListRejection
import sputter.jvm.components.contactform.datastore.ContactFormService
import sputter.jvm.components.exceptions.ValidationException
import sputter.shared.{ResponseError, ResponseOK}
import sputter.shared.contactform.{ContactForm, ContactFormApi, ContactFormResponse}


/**
  * Endpoint to let users contact you. You can optionally be notified when
  * they do.
  */
trait ContactFormRestEndpoint extends ContactFormApi {

  val contactFormService: ContactFormService

  val logger: LoggingAdapter

//  val route = path("contact") {
//    pathEndOrSingleSlash {
//      post {
//        entity(as[ContactForm]) { contactForm =>
//
//          logger.debug(s"Received contact form POST request with payload: $contactForm")
//
//          contactFormService.handleForm(contactForm = contactForm) match {
//            case Right(id) => complete(StatusCodes.OK, id)
//            case Left(e: ValidationException) => reject(
//              ValidationListRejection(message = e.message, errors = e.errors))
//            case Left(e) => throw new RuntimeException(e.message)
//          }
//        }
//      }
//    }
//  }

  override def contact(form: ContactForm) = {

    logger.debug(s"Received contact form POST request with payload: $form")

    contactFormService.handleForm(contactForm = form) match {
        // todo: wrap this in `complete`
      case Right(id) => ContactFormResponse(status = ResponseOK(), errors = List())
      // todo: wrap this in `reject`
      case Left(e: ValidationException) => ContactFormResponse(
        status = ResponseError(), errors = e.errors)
//        reject(ValidationListRejection(message = e.message, errors = e.errors))
      case Left(e) => throw new RuntimeException(e.message)
    }
  }
}
