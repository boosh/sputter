package sputter.jvm.components.contact

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._
import spray.json._
import sputter.jvm.components.contact.datastore.ContactService
import sputter.shared.ContactForm


/**
  * Created by al on 22/07/2016.
  */
class ContactFormRestEndpointTests extends WordSpec with Matchers
  with ScalatestRouteTest with JsonSupport {

  val endpoint = new ContactApiImpl(new ContactService(new SimpleContactFormDataStore()))

  "return a MethodNotAllowed error for GET requests" in {
    Get("/contact") ~> Route.seal(endpoint.route) ~> check {
      status === StatusCodes.MethodNotAllowed
      responseAs[String] shouldEqual "HTTP method not allowed, supported methods: POST"
    }
  }

  "can POST complete data to the endpoint" in {
    val form = ContactForm(body="Test string", name=Some("Me"), email=Some("me@example.com"))

    Post("/contact", form.toJson) ~> Route.seal(endpoint.route) ~> check {
      status === StatusCodes.OK
      responseAs[String] shouldEqual SimpleContactFormDataStore.SUCCESS_MESSAGE
    }
  }

// todo: fix submitting without a body
//  "body is required" in {
//    val form = Map("name" -> Some("Me"), "email" -> Some("me@example.com"))
//
//    Post("/contact", form.toJson) ~> Route.seal(endpoint.route) ~> check {
//      status === StatusCodes.BadRequest
//      responseAs[String] shouldEqual "BadRequest"
//    }
//  }

  "name is optional" in {
    val form = ContactForm(body="Test string", name=None, email=Some("me@example.com"))

    Post("/contact", form.toJson) ~> Route.seal(endpoint.route) ~> check {
      status === StatusCodes.OK
      responseAs[String] shouldEqual SimpleContactFormDataStore.SUCCESS_MESSAGE
    }
  }

  "email is optional" in {
    val form = ContactForm(body="Test string", name=Some("Me"), email=None)

    Post("/contact", form.toJson) ~> Route.seal(endpoint.route) ~> check {
      status === StatusCodes.OK
      responseAs[String] shouldEqual SimpleContactFormDataStore.SUCCESS_MESSAGE
    }
  }
}
