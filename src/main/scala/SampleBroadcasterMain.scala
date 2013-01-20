import akka.actor._

case class Message(body:String)

class SampleActor extends Actor with ActorLogging {
  import java.util.UUID

  val myId = UUID.randomUUID.toString

  def receive = {
    case message:Message => {
      println("I am "+myId+" /message:"+message.body)
      log.info("I am "+myId+" /message:"+message.body)
    }
  }
}


object SampleBroadcasterMain {
  def main(args: Array[String]) = {

   //Genereate system
    val system = ActorSystem("namespace")

    //add logListener
    val logListener = system.actorOf(Props[SampleLogListener])

    //Create actor
    val sub1 = system.actorOf(Props[SampleActor])

    //add actor to system-subscriber's network
    system.eventStream.subscribe(sub1, classOf[Message])

    //add another one -the 2nd
    system.eventStream.subscribe(system.actorOf(Props[SampleActor]), classOf[Message])

    //and add another one -the 3rd
    system.eventStream.subscribe(system.actorOf(Props[SampleActor]), classOf[Message])

    //publish messeage from here to the all subscribers.
    system.eventStream.publish(Message("hereComes! subscrivers!!"))
  }
}

 
class SampleLogListener extends Actor {
  //log
  import akka.event.Logging.InitializeLogger
  import akka.event.Logging.LoggerInitialized
  import akka.event.Logging.Error
  import akka.event.Logging.Warning
  import akka.event.Logging.Info
  import akka.event.Logging.Debug
  
  def receive = {
    case InitializeLogger(_)                        ⇒ print("initilaized")
    case Error(cause, logSource, logClass, message) ⇒ print("Err  " + message)
    case Warning(logSource, logClass, message)      ⇒ print("War  " + message)
    case Info(logSource, logClass, message)         ⇒ print("Inf  " + message)
    case Debug(logSource, logClass, message)        ⇒ print("Deb  " + message)
  }
}
