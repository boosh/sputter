package sputter.contactform

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import sputter.akkahttp.ValidationListRejection
import sputter.exceptions.ValidationException
import org.slf4j.LoggerFactory
import spray.json._


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val contactFormFormat = jsonFormat3(ContactForm)
}

/**
  * Endpoint to let users contact you. You can optionally be notified when
  * they do.
  */
class ContactFormRestEndpoint(contactFormService: ContactFormService)
  extends Directives with JsonSupport {

  val logger = LoggerFactory.getLogger(getClass)

  val route = path("contact") {
    pathEndOrSingleSlash {
      post {
        entity(as[ContactForm]) { contactForm =>

          logger.debug(s"Received contact form POST request with payload: $contactForm")

          contactFormService.handleForm(contactForm = contactForm) match {
            case Right(id) => complete(StatusCodes.OK, id)
            case Left(e: ValidationException) => reject(
              ValidationListRejection(message = e.message, errors = e.errors))
            case Left(e) => throw new RuntimeException(e.message)
          }
        }
      }
    }
  }
}
