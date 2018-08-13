package com.yarenty.streamer

import org.scalatest._

/**
  * Testing argument processing stuff.
  * (C)2018 by yarenty.
  */
class ConfigSpec extends FlatSpec {

  "Config" should "be properly instantiated if input life exist" in {
    val config: Config = new Config(Array("-in", getClass.getResource("/simple_no_header.csv").getPath))
    assert(config.csv === getClass.getResource("/simple_no_header.csv").getPath)

  }

  it should "throw NoSuchElementException if -in is not specified" in {
    assertThrows[IllegalArgumentException] {
      val config: Config = new Config(Array(""))
    }
  }

  it should "throw NoSuchElementException if -in csv file did not exits" in {
    assertThrows[IllegalArgumentException] {
      val config: Config = new Config(Array("-in", "_file_that_not_exist_"))
    }
  }
}