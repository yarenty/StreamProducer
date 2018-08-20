package com.yarenty.streamer

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.{Future, _}
import scala.concurrent.duration._


class HttpSenderSpec extends TestKit(ActorSystem("HttpSenderSpec"))
  with WordSpecLike with Matchers with MockFactory with BeforeAndAfterAll {

  implicit val config: Config = new Config(Array("-in", getClass.getResource("/simple_no_header.csv").getPath, "-url", "http://example.com"))
  implicit val actorSystem = system
  implicit val actorMaterializer = ActorMaterializer()(system)
  
  class MockHttpSender(reqRespPairs: Seq[(Uri, String)]) extends HttpGetSender {
    override implicit val ec = actorSystem.dispatcher
    
    
    val mock = mockFunction[HttpRequest, Future[HttpResponse]]
    
    override val responder: HttpResponder = mock
    
    reqRespPairs.foreach {
      case (uri, respString) =>
        val req = HttpRequest(HttpMethods.GET, uri)
        val resp = HttpResponse(status = StatusCodes.OK, entity = respString)
        mock.expects(req).returning(Future.successful(resp))
    }

   
  }
  


  "HttpSender" should {

    "Marshall responses to Strings" in {
      val mock = new MockHttpSender(Seq((Uri("http://example.com?aa"), "Response 1"), (Uri("http://example.com?a=1"), "Response 2")))
      //      Await.result(mock.send("http://example.com/1"), 1 second) should be ("Response 1")
      println(mock.send("aa")) //, true)
      println(mock.send("a=1")) //, true)
    }
  }


  override def afterAll(): Unit = {
    val termination = system.terminate()
    Await.ready(termination, Duration.Inf)
  } 

}


//
//class HttpSenderSpec extends FlatSpec {
//  implicit val config: Config = new Config(Array("-in", getClass.getResource("/simple_no_header.csv").getPath,"-url","http://122.0.0.1:8080"))
//
//  implicit val system = ActorSystem("akka-http-sample")
//  sys.addShutdownHook(system.terminate())
//
//
//  implicit val materializer = ActorMaterializer()
//  implicit val executionContext = system.dispatcher
//
//  val hello = system.actorOf(Props[HelloActor])
//
//  val routes = cors() ( new HelloService(hello).route )
//
//  Http().bindAndHandle(routes, "0.0.0.0", 12345)
//
//
//  "HttpGetSender" should "output line by line from CSV" in {
//    val sender = new HttpGetSender
//    assert(sender.send("aaa") === "January|10000.00|9000.00|1000.00")
//  }
//
//
//  it should "throw ArrayIndexOutOfBoundsException if readed more than expected line" in {
//    val cvsProcessor = new SimpleCSVProcessor
//
//    assertThrows[ArrayIndexOutOfBoundsException] {
//      cvsProcessor.getLine()
//      cvsProcessor.getLine()
//      cvsProcessor.getLine()
//      assert(cvsProcessor.getLine() === "aaa")
//    }
//  }
//}