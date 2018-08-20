package com.yarenty.streamer


import com.paulgoldbaum.influxdbclient.Parameter.{Consistency, Precision}
import com.paulgoldbaum.influxdbclient._
import org.joda.time.format.DateTimeFormat

import scala.concurrent.ExecutionContext.Implicits.global

// scalastyle:off
class InfluxDBSender(implicit config: Config) {


  val influxdb = InfluxDB.connect("localhost", 8086)

  val database = influxdb.selectDatabase("my_database")

  if (database.exists().value != None) {
    println("dropping DB")
    database.drop()
  }

  println("create DB")
  val dbcr = database.create()


//  print("drop  readings retention policy: ")
//  val retF = database.dropRetentionPolicy("readings")
//  
//  retF.onSuccess {
//    case res => println("dropped  " + res.series)
//  }
//  retF.onFailure{
//    case _ => println("not dropped")
//  }

  //database.showRetentionPolicies()

  print("re-create readings retention policy: ")
  val retFC = database.createRetentionPolicy("readings", "INF", 1, true)

  retFC.onSuccess {
    case res => println("created " + res)
  }
  retFC.onFailure{
    case _ => println("error")
  }

 println("cleaning db")
  val clean = influxdb.exec("DELETE from cpu ")

  clean.onSuccess {
    case res => println("cleaned " + res)
  }
  clean.onFailure{
    case _ => println("not cleaned")
  }
  
  
  def send(body: Array[Array[String]]) = { // scalastyle:ignore


    //    val fields = for (i <- body(0).indices) yield

    val fields = List(LongField(body(0)(1), body(1)(1).toInt),
      DoubleField(body(0)(2), body(1)(2).toDouble),
      DoubleField(body(0)(3), body(1)(3).toDouble),
      StringField(body(0)(4), body(1)(4))).toSeq

    println(fields.mkString(";\n"))

    //    val formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
    val formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
    val time = formatter.parseDateTime(body(1)(0))
    println("time:" + time.toString("yyyy-MM-dd"))

    val point = Point("cpu", time.getMillis, fields = fields).addTag("input", "csv")

    val wr = database.write(point, precision = Precision.MILLISECONDS, consistency = Consistency.ALL, retentionPolicy = "readings")

    wr.onSuccess {
      case res => println("record writen:" + res)
    }
    wr.onFailure{
      case _ => println("could not write ["+time.toString("yyyy-MM-dd")+"]")
    }

    //    null
  }

  override def finalize(): Unit = {
    influxdb.close()
    super.finalize()
  }

}
 
