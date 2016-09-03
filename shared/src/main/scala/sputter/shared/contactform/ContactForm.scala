package sputter.shared.contactform

import sputter.shared.ResponseStatus

case class ContactForm(body: String, name: Option[String], email: Option[String])

// todo: make this a StandardResponse instead and return it from several APIs
case class ContactFormResponse(status: ResponseStatus, errors: List[String])


trait ContactFormApi{
  def contact(form: ContactForm): ContactFormResponse
}
