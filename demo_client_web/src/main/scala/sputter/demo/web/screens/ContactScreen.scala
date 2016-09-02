package sputter.demo.web.screens

import org.scalajs.dom
import sputter.demo.web.styles.GlobalStyle
import sputter.js.AjaxClient
import sputter.shared.contactform.{ContactForm, ContactFormApi}
import sri.core._
import sri.scalacss.Defaults._
import sri.web.all._
import sri.web.vdom.htmltags._

import autowire._
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scalajs.concurrent.JSExecutionContext.Implicits.queue


object ContactScreen {

  @ScalaJSDefined
  class Component extends ReactComponent[Unit, Unit] {

    var bodyRef: dom.html.Input = _
    var nameRef: dom.html.Input = _
    var emailRef: dom.html.Input = _

    def handleClick(e: ReactMouseEventI) = {

      e.preventDefault()

      Option(bodyRef).foreach { body =>
        val form = ContactForm(body = body.value,
          name = Option(nameRef).map(_.value),
          email = Option(emailRef).map(_.value))

        println(s"Inside click handler with form: $form")

        AjaxClient[ContactFormApi].contact(form).call().foreach { r =>
          print(s"Received response to contact REST call: $r")
        }
      }
    }

    def render() = {
      dom.console.log("In contact screen")

      // see http://www.lihaoyi.com/hands-on-scala-js/#dom.extensions and
      // http://www.lihaoyi.com/hands-on-scala-js/#IntegratingClient-Server for making ajax calls
      // (especially note the part on autowire, http://www.lihaoyi.com/hands-on-scala-js/#Autowire)
      val contactForm = ContactForm(body = "static body", name = None, email = None)
      println(s"contact form = $contactForm")

      form()(
        div(className = GlobalStyle.flexOneAndCenter)(
          span(className = GlobalStyle.bigText)("Contact us"),
          label()("Your name",
            input(id = "name", ref = (e: dom.html.Input) => nameRef = e)),
          label()("Your email address",
            input(`type`="email", id = "email",
              ref = (e: dom.html.Input) => emailRef = e)),
          label()("Comments",
            textarea(id = "body", ref = (e: dom.html.Input) => bodyRef = e)()),
          button(id = "submit", onClick = handleClick(_: ReactMouseEventI))("Submit")
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
