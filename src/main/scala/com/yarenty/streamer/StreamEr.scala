package com.yarenty.streamer


/**
  * Stream-Er - process input CSV file and send stream of data to specific URL with configured interval.
  *
  * (C)2018 by yarenty
  */
object StreamEr {

  def main(args: Array[String]) {

    implicit val config = new Config(args)

    println("Stream-Er") // scalastyle:ignore

    val input: CSVProcessor = new CSVToHttpGetProcessor
    val sender: HttpSender = new HttpGetSender


    while (input.hasMore) {
      wait(5000)
      sender.send(input.getLine())
    }

    println("That's all folks!!") //scalastyle:ignore
   
  }
}
