package sputter.contactform

import sputter.exceptions.DataStoreException


/**
  * Created by al on 22/07/2016.
  */
class SimpleContactFormDataStore extends ContactFormDataStore{

  import SimpleContactFormDataStore._

  override def save(contactForm: ContactForm): Either[DataStoreException, String] =
    Right(SUCCESS_MESSAGE)
}

object SimpleContactFormDataStore {
  val SUCCESS_MESSAGE = "success"
}
