package sputter.jvm.components.contact

import sputter.jvm.components.utils.Sanitisation
import sputter.jvm.components.utils.Validation._
import sputter.shared.ContactForm

import scalaz._


/**
  * This would be a companion object if ContactForm was defined here. However,
  * since that's a shared class and companion objects must be defined in the
  * same file, it must be renamed.
  */
object ContactFormCompanion {
  /**
    * Performs validation on an instance, returning error messages if
    * validation fails or the object on success.
    *
    * These validations are for business logic/data constraints. We assume data
    * has already been sanitised and types parsed by the time we run these.
    *
    * @return Error messages on failure, or true if it's valid
    */
  def validate(form: ContactForm, maxBodyLength: Int = 2000): ValidationNel[String, Boolean] = {
    val validations = List(
      requireThat(form.body.length > 0)("Please enter your comment"),
      requireThat(form.body.length < maxBodyLength)(s"Your " +
        s"comment is too long. Please enter no more than $maxBodyLength characters")
    )

    reduceValidationFailures(validations)
  }

  /**
    * Returns a new object with all fields sanitised.
    *
    * @return A sanitised AppFeedbackDao object
    */
  def sanitise(form: ContactForm): ContactForm = {

    form.copy(
      body = Sanitisation.htmlToText(form.body)
    )
  }
}
