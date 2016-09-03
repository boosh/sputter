package sputter.shared

// todo: make registration form subclasses this trait be acceptable
//trait RegistrationForm

case class SimpleRegistrationForm(name: String,
                                  email: String,
                                  username: String,
                                  password: String) // extends RegistrationForm

trait RegistrationApi{
  def register(form: SimpleRegistrationForm): StandardResponse
}
