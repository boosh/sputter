package sputter.jvm.components.contact

import sputter.jvm.components.contact.datastore.ContactDataStore
import sputter.jvm.components.exceptions.DataStoreException
import sputter.shared.ContactForm


/**
  * Created by al on 22/07/2016.
  */
class SimpleContactFormDataStore extends ContactDataStore {

  import SimpleContactFormDataStore._

  override def save(contactForm: ContactForm): Either[DataStoreException, String] =
    Right(SUCCESS_MESSAGE)
}

object SimpleContactFormDataStore {
  val SUCCESS_MESSAGE = "success"
}
