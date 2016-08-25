package sputter.demo.web.screens

import org.scalajs.dom
import sputter.demo.web.styles.GlobalStyle
import sputter.shared.contactform.ContactForm
import sri.core._
import sri.scalacss.Defaults._
import sri.web.all._
import sri.web.vdom.htmltags._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U}

object ContactScreen {

  @ScalaJSDefined
  class Component extends ReactComponent[Unit, Unit] {
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
            input(id = "name")),
          label()("Your email address",
            input(`type`="email", id = "email")),
          label()("Comments",
            textarea(id = "body")()),
          button(id = "submit")("Submit")
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
