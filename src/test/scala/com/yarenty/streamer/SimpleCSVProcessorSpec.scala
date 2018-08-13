package com.yarenty.streamer

import org.scalatest._

class SimpleCSVProcessorSpec extends FlatSpec {
  implicit val config: Config = new Config(Array("-in", getClass.getResource("/simple_no_header.csv").getPath))


  "SimpleCSVParser" should "output line by line from CSV" in {
    val cvsProcessor = new SimpleCSVProcessor
    assert(cvsProcessor.getLine() === "January|10000.00|9000.00|1000.00")
    assert(cvsProcessor.getLine() === "February|11000.00|9500.00|1500.00")
    assert(cvsProcessor.getLine() === "March|12000.00|10000.00|2000.00")
  }

  it should "properly indicate if there are more lines to process" in {
    val cvsProcessor = new SimpleCSVProcessor
    cvsProcessor.getLine()
    assert(cvsProcessor.hasMore === true)
    cvsProcessor.getLine()
    assert(cvsProcessor.hasMore === true)
    cvsProcessor.getLine()
    assert(cvsProcessor.hasMore === false)
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