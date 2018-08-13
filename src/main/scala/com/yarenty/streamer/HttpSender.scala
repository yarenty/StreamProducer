package com.yarenty.streamer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.{Failure, Success}


case class Response(success: Boolean, descrition: String, `object`: Object)


abstract class HttpSender(implicit config: Config) {

  implicit val system = ActorSystem()
  implicit val dispatcher = system.dispatcher
  implicit val materializer = ActorMaterializer()


  def send(body: String):Response

}

case class HttpGetSender(implicit config: Config) extends HttpSender {

  override def send(body: String): Response = { // scalastyle:ignore

    val responseFuture: Future[HttpResponse] = Http()
      .singleRequest(HttpRequest(uri = config.url + "?" + body))

    var out: Response = null

    responseFuture.onComplete {
      case Success(res) => {
        println(res) // scalastyle:ignore
        out = new Response(true, res.toString, res)
      }
      case Failure(ex) => {
        sys.error("something wrong")
        out = new Response(false, "something wrong", ex)
      }
    }

    out
  }

}


case class HttpPostSender(implicit config: Config) extends HttpSender {


  override def send(body: String): Response = { // scalastyle:ignore

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
      method = HttpMethods.POST,
      uri = config.url,
      entity = body
    ))

    var out: Response = null

    responseFuture.onComplete {
      case Success(res) => {
        println(res) // scalastyle:ignore
        out = new Response(true, res.toString, res)
      }
      case Failure(ex) => {
        sys.error("something wrong")
        out = new Response(false, "something wrong", ex)
      }
    }

    out
  }

}
 

