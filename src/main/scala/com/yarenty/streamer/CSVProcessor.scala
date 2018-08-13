package com.yarenty.streamer

/**
  * CSV parser processors.
  *
  * (C)2018 by yarenty.
  *
  * @param config implicit
  */
abstract class CSVProcessor(implicit config: Config) {
  val src = config.csv

  def hasMore: Boolean

  def getLine(): String

}

// scalastyle:off
/**
  * Very simple parser process - just getting line by line.
  *
  * @param config implicit
  */
case class SimpleCSVProcessor(implicit config: Config) extends CSVProcessor {

  val bufferedSource = io.Source.fromFile(src)
  //yes - i know whole file in memory ... but  no need for anything fancy here
  val lines: Array[String] = bufferedSource.getLines.toArray
  var idx = 0

  override def hasMore: Boolean = lines.length > idx

  override def getLine(): String = {
    val out = lines(idx)
    idx = idx + 1
    if (config.debugMode) println(out) // scalastyle:ignore
    out
  }

  override def finalize(): Unit = {
    bufferedSource.close
    super.finalize()
  }

}

/**
  * Create line as HTTP GET parameters.  
  * TIP: CSV must have header!!!
  * TIP: Remember to add "?" sign at beginning ;-).
  *
  * @param config implicit
  */
case class CSVToHttpGetProcessor(implicit config: Config) extends CSVProcessor {


  val bufferedSource = io.Source.fromFile(src)
  //yes - i know whole file in memory ... but  no need for anything fancy here
  val lines: Array[String] = bufferedSource.getLines.toArray
  val cols = lines(0).split(",").map(_.trim)
  var idx = 1

  override def hasMore: Boolean = lines.length > idx

  override def getLine(): String = {
    val row = lines(idx).split(",").map(_.trim)
    idx = idx + 1
    val out = cols.zip(row).map(x => x._1 + "=" + x._2).mkString("&")
    if (config.debugMode) println(out) // scalastyle:ignore

    out
  }


  override def finalize(): Unit = {
    bufferedSource.close
    super.finalize()
  }

}

/**
  * Create JSON from heater as keys and each line as values.
  * TIP: CSV must have header!!!
  *
  * @TODO: Add null values.
  * @param config implicit
  */
case class CSVToJsonProcessor(implicit config: Config) extends CSVProcessor {

  val bufferedSource = io.Source.fromFile(src)
  //yes - i know whole file in memory ... but  no need for anything fancy here
  val lines: Array[String] = bufferedSource.getLines.toArray
  val cols = lines(0).split(",").map(_.trim)
  var idx = 1

  override def hasMore: Boolean = lines.length > idx

  override def getLine(): String = {
    val row = lines(idx).split(",").map(_.trim)
    idx = idx + 1
    val out = cols.zip(row).map(x => x._1 + ":" + x._2).mkString(",")
    if (config.debugMode) println(out) // scalastyle:ignore

    "{" + out + "}"
  }


  override def finalize(): Unit = {
    bufferedSource.close
    super.finalize()
  }
}