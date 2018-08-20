package com.yarenty.streamer


import com.sksamuel.elastic4s.RefreshPolicy
import com.sksamuel.elastic4s.http.{ElasticClient, ElasticProperties}
import com.sksamuel.elastic4s.http.Response
import com.sksamuel.elastic4s.http.search.SearchResponse


class ElasticSender (implicit config: Config) {


  def send(body: Array[Array[String]]) = { // scalastyle:ignore

    // you must import the DSL to use the syntax helpers
    import com.sksamuel.elastic4s.http.ElasticDsl._

    val client = ElasticClient(ElasticProperties(config.url))

//     body(0).zipWithIndex{}
//    Array.tabulate(body(0).length){ i => body(0)(i) -> body(1)(i)}

    val fields = for (i <- body(0).indices) yield body(0)(i) -> body(1)(i)

    println(fields.mkString(";\n"))

    client.execute {
      bulk(
        indexInto("test2" / "mytype").fields(fields )
      ).refresh(RefreshPolicy.WaitFor)
    }.await

    val response: Response[SearchResponse] = client.execute {
      search("test2").matchQuery("event_type", "4")
    }.await

    // prints out the original json
    println("OUTPUT::") //scalastyle:ignore
    println(response.result.hits.hits.head.sourceAsString)   // scalastyle:ignore

    client.close()

    response
    null
  }

}
 