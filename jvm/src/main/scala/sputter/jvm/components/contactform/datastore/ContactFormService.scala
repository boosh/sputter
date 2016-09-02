package sputter.jvm.components.contactform.datastore

import sputter.jvm.components.contactform.ContactFormCompanion
import sputter.jvm.components.exceptions.{AkkaHttpExtensionsException, DataStoreException, ValidationException}
import sputter.shared.contactform.ContactForm

import scalaz.Scalaz._
import scalaz._

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

    val sanitisedForm = ContactFormCompanion.sanitise(contactForm = contactForm)
    ContactFormCompanion.validate(contactForm = sanitisedForm) match {
      case Success(_) => contactFormDataStore.save(sanitisedForm)
      case Failure(e) =>
        Left(ValidationException(message = "Error validating contact form",
          errors = e.toList))
    }
  }
}
