package sputter.shared.contactform

import sputter.shared.ResponseStatus

case class ContactForm(body: String, name: Option[String], email: Option[String])
case class ContactFormResponse(status: ResponseStatus, errors: List[String])


trait ContactFormApi{
  def contact(form: ContactForm): ContactFormResponse
}
