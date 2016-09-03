package sputter.jvm.components.registration

import sputter.jvm.components.utils.Sanitisation
import sputter.jvm.components.utils.Validation._
import sputter.shared.SimpleRegistrationForm

import scalaz._

// todo: this should be converted to implicits so users can provied their own
// implementations easily, and so it'll work with subclasses of RegistrationForm

/**
  * This would be a companion object if ContactForm was defined here. However,
  * since that's a shared class and companion objects must be defined in the
  * same file, it must be renamed.
  */
object SimpleRegistrationFormCompanion {
  /**
    * Performs validation on an instance, returning error messages if
    * validation fails or the object on success.
    *
    * These validations are for business logic/data constraints. We assume data
    * has already been sanitised and types parsed by the time we run these.
    *
    * @return Error messages on failure, or true if it's valid
    */
  def validate(form: SimpleRegistrationForm, maxNameLength: Int = 50,
               maxEmailLength: Int = 100, maxUsernameLength: Int = 50,
               maxPasswordLength: Int = 100): ValidationNel[String, Boolean] = {
    val validations = List(
      requireThat(form.name.length > 0)("Please enter your name"),
      requireThat(form.email.length > 0)("Please enter your email address"),
      requireThat(form.username.length > 0)("Please enter a username"),
      requireThat(form.password.length > 0)("Please enter a password"),
      
      requireThat(form.name.length < maxNameLength)(s"Your " +
        s"name is too long. Please enter no more than $maxNameLength characters"),
      requireThat(form.email.length < maxEmailLength)(s"Your " +
        s"email is too long. Please enter no more than $maxEmailLength characters"),
      requireThat(form.username.length < maxUsernameLength)(s"Your " +
        s"username is too long. Please enter no more than $maxUsernameLength characters"),
      requireThat(form.password.length < maxPasswordLength)(s"Your " +
        s"password is too long. Please enter no more than $maxPasswordLength characters")
    )

    reduceValidationFailures(validations)
  }

  /**
    * Returns a new object with all fields sanitised.
    *
    * @return A sanitised AppFeedbackDao object
    */
  def sanitise(form: SimpleRegistrationForm): SimpleRegistrationForm = {

    form.copy(
      name = Sanitisation.htmlToText(form.name),
      email = Sanitisation.htmlToText(form.email),
      username = Sanitisation.htmlToText(form.username),
      password = Sanitisation.htmlToText(form.password)
    )
  }
}
