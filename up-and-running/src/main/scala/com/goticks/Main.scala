package com.goticks
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.{implicitConversions, postfixOps}
import scala.util.{Failure, Success}

object Main extends App with RequestTimeout {

  val config = ConfigFactory.load()
  val host   = config.getString("http.host") // Gets the host and a port from the configuration
  val port   = config.getInt("http.port")

  implicit val system: ActorSystem          = ActorSystem() // ActorMaterializer requires an implicit ActorSystem
  implicit val ec: ExecutionContextExecutor = system.dispatcher // bindingFuture.map requires an implicit ExecutionContext

  val api = new RestApi(system, requestTimeout(config)).routes // the RestApi provides a Route

  implicit val materializer: ActorMaterializer = ActorMaterializer()                   // bindAndHandle requires an implicit materializer
  val bindingFuture: Future[ServerBinding]     = Http().bindAndHandle(api, host, port) //Starts the HTTP server

  val log = Logging(system.eventStream, "go-ticks")

  bindingFuture
    .map { serverBinding =>
      log.info(s"RestApi bound to ${serverBinding.localAddress} ")
    }
    .onComplete {
      case Success(_) =>
      case Failure(ex) =>
        log.error(ex, f"Failed to bind to $host:$port!")
        system.terminate()
    }

}

trait RequestTimeout {
  import scala.concurrent.duration._
  def requestTimeout(config: Config): Timeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
