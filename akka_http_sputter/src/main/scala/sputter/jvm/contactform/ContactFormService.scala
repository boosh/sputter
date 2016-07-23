package sputter.contactform

import sputter.exceptions.{AkkaHttpExtensionsException, DataStoreException, ValidationException}

import scalaz._
import scalaz.Scalaz._

/**
  * Interface for interacting with a datastore
  */
trait ContactFormDataStore {
  // todo: this should return a Future
  def save(contactForm: ContactForm): Either[DataStoreException, String]
}

/**
  * Handles form validation and saving it to a backend
  */
class ContactFormService(contactFormDataStore: ContactFormDataStore) {

  def handleForm(contactForm: ContactForm): Either[AkkaHttpExtensionsException, String] = {

    val sanitisedForm = ContactForm.sanitise(contactForm = contactForm)
    ContactForm.validate(contactForm = sanitisedForm) match {
      case Success(_) => contactFormDataStore.save(sanitisedForm)
      case Failure(e) =>
        Left(ValidationException(message = "Error validating contact form",
          errors = e.toList))
    }
  }
}
