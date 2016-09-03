package sputter.shared

/**
  * The actual API implemented by the demo client and server.
  * todo: Create a new repo for the demos, with a new cross-project config.
  * Move this into the shared library of that repo.
  */
trait Api extends ContactApi with RegistrationApi
