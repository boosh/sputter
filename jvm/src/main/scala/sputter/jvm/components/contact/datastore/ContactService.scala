package sputter.jvm.components.contact.datastore

import sputter.jvm.components.contact.ContactFormCompanion
import sputter.jvm.components.exceptions.{AkkaHttpExtensionsException, DataStoreException, ValidationException}
import sputter.shared.ContactForm

import scalaz.Scalaz._
import scalaz._

/**
  * Interface for interacting with a datastore
  */
trait ContactDataStore {
  // todo: this should return a Future
  def save(form: ContactForm): Either[DataStoreException, String]
}


/**
  * Trait to handle the form
  */
trait ContactService {
  def handleForm(form: ContactForm): Either[AkkaHttpExtensionsException, String]
}

/**
  * Default implementation. Handles form validation and saving it to a
  * data store
  *
  * @param contactDataStore
  */
class ContactServiceImpl(contactDataStore: ContactDataStore) extends ContactService {

  def handleForm(form: ContactForm): Either[AkkaHttpExtensionsException, String] = {

    val sanitisedForm = ContactFormCompanion.sanitise(form = form)
    ContactFormCompanion.validate(form = sanitisedForm) match {
      case Success(_) => contactDataStore.save(sanitisedForm)
      case Failure(e) =>
        Left(ValidationException(message = "Error validating contact form",
          errors = e.toList))
    }
  }
}
