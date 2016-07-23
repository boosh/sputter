package sputter.jvm.components.messaging

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives

import scala.concurrent.ExecutionContext

/**
  * Lets users send realtime user-to-user chat messages to each other, or to
  * receive realtime system notifications.
  */
class RealtimeMessagingEndpoint(realtimeMessagingActor: ActorRef)
                               (implicit executionContext: ExecutionContext)
  extends Directives {

}
