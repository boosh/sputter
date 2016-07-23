package sputter.jvm.datastores.mock.contactform

import sputter.jvm.components.contactform.ContactFormDataStore
import sputter.jvm.components.exceptions.DataStoreException
import sputter.shared.contactform.ContactForm

/**
  * Created by al on 23/07/2016.
  */
class ContactFormMockDataStore extends ContactFormDataStore {

  override def save(contactForm: ContactForm): Either[DataStoreException, String] = ???
}
