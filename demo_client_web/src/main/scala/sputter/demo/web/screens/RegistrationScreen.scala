package sputter.demo.web.screens

import autowire._
import org.scalajs.dom
import sputter.demo.web.styles.GlobalStyle
import sputter.js.AjaxClient
import sputter.shared.{Api, SimpleRegistrationForm}
import sri.core._
import sri.scalacss.Defaults._
import sri.web.all._
import sri.web.vdom.htmltags._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined


object RegistrationScreen {

  @ScalaJSDefined
  class Component extends ReactComponent[Unit, Unit] {

    var nameRef: dom.html.Input = _
    var emailRef: dom.html.Input = _
    var usernameRef: dom.html.Input = _
    var passwordRef: dom.html.Input = _

    def handleClick(e: ReactMouseEventI) = {

      e.preventDefault()

      val form = SimpleRegistrationForm(
        name = nameRef.value,
        username = usernameRef.value,
        password = passwordRef.value,
        email = emailRef.value)

      println(s"Inside click handler with form: $form")

      AjaxClient[Api].register(form).call().foreach { r =>
        dom.console.log(s"Received response to REST call: $r")

        // todo: show a success message on success, or the list of error
        // messages on error
      }
    }

    def render() = {
      form()(
        div(className = GlobalStyle.flexOneAndCenter)(
          span(className = GlobalStyle.bigText)("Register"),
          label()("Your name",
            input(id = "name", ref = (e: dom.html.Input) => nameRef = e)),
          label()("Your email address",
            input(`type`="email", id = "email",
              ref = (e: dom.html.Input) => emailRef = e)),
          label()("User name",
            input(id = "username", ref = (e: dom.html.Input) => usernameRef = e)),
          label()("Password",
            input(`type`="password", id = "password",
              ref = (e: dom.html.Input) => passwordRef = e)),
          button(id = "submit", onClick = handleClick(_: ReactMouseEventI))("Register")
        )
      )
    }
  }

  val constructor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(key: js.UndefOr[String] = js.undefined,
            ref: js.Function1[Component, _] = null) = {
    createElementNoProps(constructor, key = key, ref = ref)
  }
}
