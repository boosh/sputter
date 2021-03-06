package sputter.jvm.components.registration.datastore

import sputter.jvm.components.exceptions.{AkkaHttpExtensionsException, DataStoreException, ValidationException}
import sputter.shared.SimpleRegistrationForm

import scalaz.{Failure, Success}

/**
  * Interface for interacting with a datastore
  */
trait RegistrationDataStore {
  // todo: this should return a Future
  def save(form: SimpleRegistrationForm): Either[DataStoreException, String]
}


/**
  * Trait to handle the form
  */
trait RegistrationService {
  def handleForm(form: SimpleRegistrationForm): Either[AkkaHttpExtensionsException, String]
}


/**
  * Default implementation. Handles form validation and saving it to a
  * data store
  *
  * @param registrationDataStore
  */
class RegistrationServiceImpl(registrationDataStore: RegistrationDataStore)
  extends RegistrationService {

  def handleForm(form: SimpleRegistrationForm): Either[AkkaHttpExtensionsException, String] = {

    registrationDataStore.save(form)

    // todo: rework this so it works with user-defined subclasses of RegistrationForm
//    val sanitisedForm = RegistrationFormCompanion.sanitise(form = form)
//    RegistrationFormCompanion.validate(form = sanitisedForm) match {
//      case Success(_) => registrationDataStore.save(sanitisedForm)
//      case Failure(e) =>
//        Left(ValidationException(message = "Error validating registration form",
//          errors = e.toList))
//    }
  }
}
