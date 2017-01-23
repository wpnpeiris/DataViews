package se.kth

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.apache.spark.SparkContext

class TestService(val sc: SparkContext)  {
      val test = "Test Val"
      val logData = sc.textFile("C:\\tools\\spark-2.1.0-bin-hadoop2.7\\README.md", 2).cache()
      
      val route =
      path("init") {
        get {
          val logFile = "YOUR_SPARK_HOME/README.md"
          
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Initiatlised</h1>"))
        }
      }~
      path("count") {
        get {
          parameters('key.as[String]) { (key) =>
              complete {
                val numAs = logData.filter(line => line.contains(key)).count()
                HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>" + numAs + "</h1>")      
              }
          }
          
        }
      }  
}