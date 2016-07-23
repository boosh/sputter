package sputter.jvm.components.auth

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val credentialsFormat = jsonFormat3(Credentials)
}

/**
  * Endpoint for registering and authenticating users. To create a non-REST
  * endpoint, wrap the AuthActor directly.
  */
class AuthRestEndpoint(authService: AuthService) extends Directives with JsonSupport {

  val logger = LoggerFactory.getLogger(getClass)

  val route =
    path("auth") {
//      path("register") {
//        pathEndOrSingleSlash {
//          post {
//            complete("handle registration")
//            //        handleWith { sm: SendMessage => messenger ! sm; "{}" }
//          }
//        }
//      } ~
        path("login") {
          pathEndOrSingleSlash {
            post {
              entity(as[Credentials]) { credentials =>

                logger.debug(s"Received login POST request for " +
                  s"user ${credentials.id} and provider ${credentials.provider}")

                authService.handleLogin(credentials = credentials)
//                match {
                // todo: if the credentials are valid, map over the result to
                // generate a JWT to speed up validation on future requests.
//                  case Right(id) => complete(StatusCodes.OK, id)
//
//                }

                complete("handle login")
                //        handleWith { sm: SendMessage => messenger ! sm; "{}" }
              }
            }
          }
        } ~
        path("logout") {
          pathEndOrSingleSlash {
            get {
              complete("handle logout")
              //        handleWith { sm: SendMessage => messenger ! sm; "{}" }
            }
          }
        }
    }
}
