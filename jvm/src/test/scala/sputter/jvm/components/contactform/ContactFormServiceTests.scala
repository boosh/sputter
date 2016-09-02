package sputter.jvm.components.contactform

import org.scalatest._
import sputter.jvm.components.contactform.datastore.{ContactFormDataStore, ContactFormService}
import sputter.jvm.components.exceptions.DataStoreException
import sputter.shared.contactform.ContactForm


class BrokenContactFormDataStore extends ContactFormDataStore {

  override def save(contactForm: ContactForm): Either[DataStoreException, String] =
    Left(DataStoreException("Test error"))
}

/**
  * Created by al on 22/07/2016.
  */
class ContactFormServiceTests extends WordSpec with Matchers with JsonSupport {

  val brokenService = new ContactFormService(new BrokenContactFormDataStore())
  val workingService = new ContactFormService(new SimpleContactFormDataStore())

  "successful results are returned from the persistence layer" in {

    val result = workingService.handleForm(ContactForm(body="test body", name=None, email=None))
    result shouldEqual Right(SimpleContactFormDataStore.SUCCESS_MESSAGE)
  }

  "errors are returned from the persistence layer" in {

    val result = brokenService.handleForm(ContactForm(body="test body", name=None, email=None))
    result shouldEqual Left(DataStoreException("Test error"))
  }
}
