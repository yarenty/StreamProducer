package com.yarenty.streamer

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import org.scalatest._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import akka.actor.{Actor, ActorLogging}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives
import akka.util.Timeout
import com.yarenty.streamer.HelloActor.{AnonymousHello, Greeting}


object HelloActor {
  case object AnonymousHello
  case class Hello(name: String)
  case class Greeting(greeting: String)
}
class HelloActor extends Actor with ActorLogging {
  import HelloActor._

  def receive: Receive = {
    case AnonymousHello => { sender ! Greeting("Hello") }
    case Hello(name) => sender ! Greeting(s"Hello, $name")
  }
}

class HelloService(hello: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives {

  implicit val timeout = Timeout(2.seconds)

  val route = getHello 


  def getHello =
    path("hello") {
      get {
        complete { (hello ? AnonymousHello).mapTo[Greeting] }
      }
    }
}

class HttpSenderSpec extends FlatSpec {
  implicit val config: Config = new Config(Array("-in", getClass.getResource("/simple_no_header.csv").getPath,"-url","http://122.0.0.1:8080"))

  implicit val system = ActorSystem("akka-http-sample")
  sys.addShutdownHook(system.terminate())


  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val hello = system.actorOf(Props[HelloActor])

  val routes = cors() ( new HelloService(hello).route )

  Http().bindAndHandle(routes, "0.0.0.0", 12345)


  "HttpGetSender" should "output line by line from CSV" in {
    val sender = new HttpGetSender
    assert(sender.send("aaa") === "January|10000.00|9000.00|1000.00")
  }


  it should "throw ArrayIndexOutOfBoundsException if readed more than expected line" in {
    val cvsProcessor = new SimpleCSVProcessor

    assertThrows[ArrayIndexOutOfBoundsException] {
      cvsProcessor.getLine()
      cvsProcessor.getLine()
      cvsProcessor.getLine()
      assert(cvsProcessor.getLine() === "aaa")
    }
  }
}