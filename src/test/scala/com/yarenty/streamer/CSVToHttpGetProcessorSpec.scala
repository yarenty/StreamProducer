package com.yarenty.streamer

import org.scalatest._

class CSVToHttpGetProcessorSpec extends FlatSpec {
  implicit val config: Config = new Config(Array("-in", getClass.getResource("/simple_with_header.csv").getPath))


  "CSVToHttpGetParser" should "output line by line from CSV" in {
    val cvsProcessor = new CSVToHttpGetProcessor
    assert(cvsProcessor.getLine() === "Month=January&Income=10000.00&Expenses=9000.00&Profit=1000.00")
    assert(cvsProcessor.getLine() === "Month=February&Income=11000.00&Expenses=9500.00&Profit=1500.00")
    assert(cvsProcessor.getLine() === "Month=March&Income=12000.00&Expenses=10000.00&Profit=2000.00")
  }

  it should "properly indicate if there are more lines to process" in {
    val cvsProcessor = new CSVToHttpGetProcessor
    cvsProcessor.getLine()
    assert(cvsProcessor.hasMore === true)
    cvsProcessor.getLine()
    assert(cvsProcessor.hasMore === true)
    cvsProcessor.getLine()
    assert(cvsProcessor.hasMore === false)
  }


  it should "throw ArrayIndexOutOfBoundsException if readed more than expected line" in {
    val cvsProcessor = new CSVToHttpGetProcessor

    assertThrows[ArrayIndexOutOfBoundsException] {
      cvsProcessor.getLine()
      cvsProcessor.getLine()
      cvsProcessor.getLine()
      assert(cvsProcessor.getLine() === "aaa")
    }
  }
}