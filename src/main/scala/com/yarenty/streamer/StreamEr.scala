package com.yarenty.streamer


/**
  * Stream-Er - process input CSV file and send stream of data to specific URL with configured interval.
  * 
  * (C)2018 by yarenty
  */
object StreamEr  {
  
 def main(args: Array[String]) {

   implicit val config = new Config(args)
   
   println("Stream-Er") // scalastyle:ignore

 }  
}
