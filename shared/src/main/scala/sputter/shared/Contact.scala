package sputter.shared

case class ContactForm(body: String, name: Option[String], email: Option[String])

trait ContactApi{
  def contact(form: ContactForm): StandardResponse
}
