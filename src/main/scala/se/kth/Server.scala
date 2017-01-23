package se.kth

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.Http
import scala.io.StdIn
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Server {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()

    implicit val exeuctionContext = system.dispatcher;

    val conf = new SparkConf()
    conf.set("spark.app.name", "Test App")
    conf.set("spark.ui.port", "36000")
    
    val sc = new SparkContext(conf)
    
    val bindingFuture = Http().bindAndHandle(new TestService(sc).route, "localhost", 9090)

    println(s"Server online at http://localhost:9090/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}