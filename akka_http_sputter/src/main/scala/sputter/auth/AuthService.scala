package sputter.auth

import sputter.auth.providers.{FacebookProvider, LocalProvider}
import sputter.exceptions.{AkkaHttpExtensionsException, DataStoreException}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
  * Interface for interacting with a datastore
  */
trait AuthDataStore {

}


object AuthProvidersEnum extends Enumeration {
  val LOCAL, FACEBOOK = Value
}

/**
  * Handles form validation and delegating to a class that interacts with a
  * storage backend
  */
class AuthService {

  val logger = LoggerFactory.getLogger(getClass)

  val conf = ConfigFactory.load()

  /**
    * Validate credentials and then delegate authentication to the relevant
    * provider
    *
    * @param credentials
    */
  def handleLogin(credentials: Credentials): Either[AkkaHttpExtensionsException, String] = {

    ???
//    credentials.provider match {
//      case p if p == AuthProvidersEnum.LOCAL.toString &&
//        conf.getBoolean("auth.providers.local") =>
//        LocalProvider.authenticate(credentials = credentials)
//      case p if p == AuthProvidersEnum.FACEBOOK.toString &&
//        conf.getBoolean("auth.providers.facebook") =>
//        FacebookProvider.authenticate(credentials = credentials)
//      case _ =>
//        logger.warn(s"No provider enabled for ${credentials.provider}")
//    }
  }

  /**
    * Log the user out, possibly returning errors.
    *
    * @return
    */
  def handleLogout(): Option[DataStoreException] = ???
}
