package sputter.jvm.datastores.mock.contact

import org.slf4j.LoggerFactory
import sputter.jvm.components.contact.datastore.ContactDataStore
import sputter.jvm.components.exceptions.DataStoreException
import sputter.shared.ContactForm

/**
  * Datastore implementation that just logs submissions
  */
class ContactMockDataStore extends ContactDataStore {

  val logger = LoggerFactory.getLogger(getClass)

  override def save(form: ContactForm): Either[DataStoreException, String] = {
    logger.info(s"Received form: $form")
    Right("Mock form ID 1")
  }
}
