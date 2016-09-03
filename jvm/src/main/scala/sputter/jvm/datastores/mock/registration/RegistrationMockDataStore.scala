package sputter.jvm.datastores.mock.registration

import org.slf4j.LoggerFactory
import sputter.jvm.components.exceptions.DataStoreException
import sputter.jvm.components.registration.datastore.RegistrationDataStore
import sputter.shared.RegistrationForm

/**
  * Datastore implementation that just logs submissions
  */
class RegistrationMockDataStore extends RegistrationDataStore {

  val logger = LoggerFactory.getLogger(getClass)

  override def save(form: RegistrationForm): Either[DataStoreException, String] = {
    logger.info(s"Received form: $form")
    Right("Mock form ID 1")
  }
}
