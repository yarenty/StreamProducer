package com.yarenty.streamer

import org.scalatest._

class CSVToJsonProcessorSpec extends FlatSpec {
  implicit val config: Config = new Config(Array("-in", getClass.getResource("/simple_with_header.csv").getPath))


  "SCSVToJsonParser" should "output line by line from CSV" in {
    val cvsProcessor = new CSVToJsonProcessor
    assert(cvsProcessor.getLine() === "{Month:January,Income:10000.00,Expenses:9000.00,Profit:1000.00}")
    assert(cvsProcessor.getLine() === "{Month:February,Income:11000.00,Expenses:9500.00,Profit:1500.00}")
    assert(cvsProcessor.getLine() === "{Month:March,Income:12000.00,Expenses:10000.00,Profit:2000.00}")
  }

  it should "properly indicate if there are more lines to process" in {
    val cvsProcessor = new CSVToJsonProcessor
    cvsProcessor.getLine()
    assert(cvsProcessor.hasMore === true)
    cvsProcessor.getLine()
    assert(cvsProcessor.hasMore === true)
    cvsProcessor.getLine()
    assert(cvsProcessor.hasMore === false)
  }


  it should "throw ArrayIndexOutOfBoundsException if readed more than expected line" in {
    val cvsProcessor = new CSVToJsonProcessor

    assertThrows[ArrayIndexOutOfBoundsException] {
      cvsProcessor.getLine()
      cvsProcessor.getLine()
      cvsProcessor.getLine()
      assert(cvsProcessor.getLine() === "aaa")
    }
  }
}