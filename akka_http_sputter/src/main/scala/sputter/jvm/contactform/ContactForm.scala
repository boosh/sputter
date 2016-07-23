package sputter.contactform

import sputter.utils.Sanitisation
import sputter.utils.Validation._
import scalaz._


object ContactForm {
  /**
    * Performs validation on an instance, returning error messages if
    * validation fails or the object on success.
    *
    * These validations are for business logic/data constraints. We assume data
    * has already been sanitised and types parsed by the time we run these.
    *
    * @return Error messages on failure, or true if it's valid
    */
  def validate(contactForm: ContactForm, maxBodySize: Int = 2000): ValidationNel[String, Boolean] = {
    val validations = List(
      requireThat(contactForm.body.length > 0)("Please enter your comment"),
      requireThat(contactForm.body.length < maxBodySize)(s"Your " +
        s"comment is too long. Please enter no more than $maxBodySize characters")
    )

    reduceValidationFailures(validations)
  }

  /**
    * Returns a new object with all fields sanitised.
    *
    * @return A sanitised AppFeedbackDao object
    */
  def sanitise(contactForm: ContactForm): ContactForm = {

    contactForm.copy(
      body = Sanitisation.htmlToText(contactForm.body)
    )
  }
}
