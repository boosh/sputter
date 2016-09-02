import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import sputter.jvm.components.RestRouter
import sputter.jvm.components.contactform.ContactFormRestEndpoint
import sputter.jvm.components.contactform.datastore.ContactFormService
import sputter.jvm.datastores.mock.contactform.ContactFormMockDataStore
import akka.http.scaladsl.server.Directives._
import sputter.shared.contactform.ContactFormApi
import lib.CorsSupport
import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Max-Age`}


/**
  * Demo server using sputter components.
  *
  * Run this with `sbt "project demo_jvm" run` from the directory containing
  * `build.sbt`, or build a fat jar with `sbt "project demo_jvm" assembly`.
  */
object Server extends App with CorsSupport with ContactFormRestEndpoint {

  // CORS config
  override val corsAllowOrigins: List[String] = List("http://localhost:8090")

  override val corsAllowedHeaders: List[String] = List("Origin", "Authorization", "Content-Type",
    "Accept", "Accept-Encoding", "Accept-Language", "Host", "Referer", "User-Agent")

  override val corsAllowCredentials: Boolean = false

  override val optionsCorsHeaders: List[HttpHeader] = List[HttpHeader](
    `Access-Control-Allow-Headers`(corsAllowedHeaders.mkString(", ")),
    `Access-Control-Max-Age`(60 * 60 * 24 * 20), // cache pre-flight response for 20 days
    `Access-Control-Allow-Credentials`(corsAllowCredentials)
  )

  val conf = ConfigFactory.load()

  implicit val system = ActorSystem("sputter-demo-server")
  implicit val materializer = ActorMaterializer()

  val logger = Logging(system, getClass)

  val contactFormService = new ContactFormService(new ContactFormMockDataStore())

  /**
    * Routes with mock logging endpoints. Test them with:
    *
    * curl -H "Content-Type: application/json" -X POST \
    *   -d '{"body":"test contact form body", "name": "Me", "email": "me@example.com"}' \
    *   http://SERVER_HOST:SERVER_PORT/contact
    *
    * Mix and match the components to use by combining them with a ~.
    * Either use the provided data stores or write your own implementing the
    * necessary traits.
    */
//  val route = new ContactFormRestEndpoint(
//    new ContactFormService(new ContactFormMockDataStore())
//  ).route
  // ~ nextRoute...

  val route = get {
    pathSingleSlash {
      complete("OK")
    }
  } ~
  post {
    path(Segments) { s =>
      entity(as[String]) { e =>
        complete {
          RestRouter.route[ContactFormApi](Server)(
            autowire.Core.Request(
              s,
              upickle.default.read[Map[String, String]](e)
            )
          )
        }
      }
    }
  }

  /**
    * Ensure that the constructed ActorSystem is shut down when the JVM shuts down
    */
  sys.addShutdownHook(system.terminate())

  val serverHost = conf.getString("server.host")
  val serverPort = conf.getInt("server.port")

  Http().bindAndHandle(route, serverHost, serverPort)
  logger.info(s"Server running at $serverHost:$serverPort")
}
