package com.yarenty.streamer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer


/**
  * Stream-Er - process input CSV file and send stream of data to specific URL with configured interval.
  *
  * (C)2018 by yarenty
  */
object StreamEr {

  def main(args: Array[String]) {

    implicit val config = new Config(args)

      implicit val system = ActorSystem()
      implicit val dispatcher = system.dispatcher
      implicit val materializer = ActorMaterializer()
   
    
    println("Stream-Er") // scalastyle:ignore

//    val input: CSVProcessor = new CSVToHttpGetProcessor

    val input = new CSVToArrayProcessor
    val sender: HttpSender = new HttpGetSender
    
    val influxDBSend: InfluxDBSender = new InfluxDBSender
    
    
    while (input.hasMore) {
      Thread.sleep(5000)
//      sender.send(input.getLine())

      influxDBSend.send(input.getArray)
    }

    println("That's all folks!!") //scalastyle:ignore
   
    System.exit(0)
  }
}
