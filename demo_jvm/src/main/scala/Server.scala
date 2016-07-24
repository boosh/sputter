import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import sputter.jvm.components.contactform.{ContactFormRestEndpoint, ContactFormService}
import sputter.jvm.datastores.mock.contactform.ContactFormMockDataStore

/**
  * Demo server using sputter components.
  *
  * Run this with `sbt "project demo_jvm" run` from the directory containing
  * `build.sbt`, or build a fat jar with `sbt "project demo_jvm" assembly`.
  */
object Server extends App {

  val conf = ConfigFactory.load()

  implicit val system = ActorSystem("sputter-demo-server")
  implicit val materializer = ActorMaterializer()

  val logger = Logging(system, getClass)

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
  val route = new ContactFormRestEndpoint(
    new ContactFormService(new ContactFormMockDataStore())
  ).route
  // ~ nextRoute...

  /**
    * Ensure that the constructed ActorSystem is shut down when the JVM shuts down
    */
  sys.addShutdownHook(system.terminate())

  val serverHost = conf.getString("server.host")
  val serverPort = conf.getInt("server.port")

  Http().bindAndHandle(route, serverHost, serverPort)
  logger.info(s"Server running at $serverHost:$serverPort")
}
