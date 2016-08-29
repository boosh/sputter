package sputter.shared.contactform

import sputter.shared.ResponseStatus

case class ContactForm(body: String, name: Option[String], email: Option[String])
case class ContactFormResponse(status: ResponseStatus, message: Option[String])

//trait ContactFormApi{
//  def submit(form: ContactForm): Seq[ContactFormResponse]
//}
