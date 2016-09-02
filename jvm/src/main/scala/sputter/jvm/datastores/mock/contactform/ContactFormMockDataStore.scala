package sputter.jvm.datastores.mock.contactform

import org.slf4j.LoggerFactory
import sputter.jvm.components.contactform.datastore.ContactFormDataStore
import sputter.jvm.components.exceptions.DataStoreException
import sputter.shared.contactform.ContactForm

/**
  * Datastore implementation that just logs submissions
  */
class ContactFormMockDataStore extends ContactFormDataStore {

  val logger = LoggerFactory.getLogger(getClass)

  override def save(contactForm: ContactForm): Either[DataStoreException, String] = {
    logger.info(s"Received form: $contactForm")
    Right("Mock form ID 1")
  }
}
