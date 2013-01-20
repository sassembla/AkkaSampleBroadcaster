import akka.actor._

case class Message(body:String)

class SampleActor extends Actor {
  import java.util.UUID

  val myId = UUID.randomUUID.toString

  def receive = {
    case message:Message => println("I am "+myId+" /message:"+message.body)    
  }
}


object SampleBroadcasterMain {
  def main(args: Array[String]) = {

   //Genereate system
    val system = ActorSystem("namespace")

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
