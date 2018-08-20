package com.yarenty.streamer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.util.{Failure, Success}
import scala.concurrent.{ExecutionContext, Future}

case class Output(success: Boolean, descrition: String, `object`: Object)

//
//abstract class HttpSender(implicit config: Config) {
//
//  implicit val system = ActorSystem()
//  implicit val dispatcher = system.dispatcher
//  implicit val materializer = ActorMaterializer()
//
//
//  def send(body: String):Output
//
//}


trait HttpSender  {

  type HttpResponder = HttpRequest => Future[HttpResponse]

  def responder: HttpResponder

  implicit def actorSystem: ActorSystem

  implicit def actorMaterializer: ActorMaterializer

  implicit def ec: ExecutionContext

//  def sampleTextFile(uri: Uri): Future[String] = {
//
//    val responseF = responder(HttpRequest(uri = uri))
//    responseF.flatMap { response => Unmarshal(response.entity).to[String] }
//  }

   def send(body: String): Output
}


case class HttpGetSender(implicit config: Config, val actorSystem:ActorSystem,  val actorMaterializer: ActorMaterializer) extends HttpSender {

  override implicit val ec: ExecutionContext = actorSystem.dispatcher

  override def responder = Http().singleRequest(_)
  
  
  override def send(body: String): Output = { // scalastyle:ignore

    val uri = config.url + "?" + body
    println("Call: " + uri) // scalastyle:ignore
    val responseFuture: Future[HttpResponse] = responder(HttpRequest(uri = uri))
    
    var out: Output = null

    responseFuture.onComplete {
      case Success(res) => {
        println(res) // scalastyle:ignore
        out = new Output(true, res.toString, res)
      }
      case Failure(ex) => {
        sys.error("something wrong")
        out = new Output(false, "something wrong", ex)
      }
    }

    out
  }

}

/*
case class HttpPostSender(implicit config: Config, implicit val actorSystem:ActorSystem, implicit val actorMaterializer: ActorMaterializer) extends HttpSender {

  override val ec: ExecutionContext = actorSystem.dispatcher

  override def responder = Http().singleRequest(_)

  override def send(body: String): Output = { // scalastyle:ignore

    val responseFuture: Future[HttpResponse] = responder(HttpRequest(
      method = HttpMethods.POST,
      uri = config.url,
      entity = body
    ))

    var out: Output = null

    responseFuture.onComplete {
      case Success(res) => {
        println(res) // scalastyle:ignore
        out = new Output(true, res.toString, res)
      }
      case Failure(ex) => {
        sys.error("something wrong")
        out = new Output(false, "something wrong", ex)
      }
    }

    out
  }

}

*/
