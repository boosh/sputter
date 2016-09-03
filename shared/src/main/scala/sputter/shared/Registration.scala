package sputter.shared

// Any registration form can be used that subclasses this trait
trait RegistrationForm

case class SimpleRegistrationForm(name: String,
                                  email: String,
                                  username: String,
                                  password: String) extends RegistrationForm

trait RegistrationApi{
  def register(form: RegistrationForm): StandardResponse
}
