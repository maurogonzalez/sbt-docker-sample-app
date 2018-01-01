package com.maurogonzalez

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.separateOnSlashes
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import ch.megard.akka.http.cors.CorsDirectives._
import com.maurogonzalez.services.{NameService, StatusService, SwaggerDocService}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object Main extends App with HttpService with RequestLogging {

  override implicit def executor = system.dispatcher
  protected def log = Logging(system, service)
  override implicit val system: ActorSystem = ActorSystem()
  override implicit val materializer: Materializer = ActorMaterializer()

  override protected def configureRoutes(system: ActorSystem): Route = {
    implicit val s = system
    cors()(StatusService().routes() ~ NameService().routes())
  }
  override def akkaConfig: Config = ConfigFactory.load()

  def bind(route: Route, interface: String, basePath: String): Int ⇒ Boolean =
    (port: Int) ⇒ {
      val mainRouter = pathPrefix(separateOnSlashes(basePath)) {
        route ~ SwaggerDocService("localhost", port, system, basePath, akkaConfig.getString("swagger.host")).routes
      }

      val eventualBinding = Http().bindAndHandle(mainRouter, interface, port)
      Try(Await.result(eventualBinding, Duration(60, "seconds"))) match {
        case Failure(t) ⇒
          log.error("Error binding server", t.asInstanceOf[Exception])
          true
        case Success(_) ⇒
          log.info(
            s"""Server bound correctly
            Listening on port $port with binding $interface
            Try curl http://$interface:$port/$basePath/status"""
          )
          false
      }
    }

  val portFrom: Int = akkaConfig.getInt("akka.http.ports.from")
  val portTo: Int = akkaConfig.getInt("akka.http.ports.to")

  scala.util.Random.shuffle(scala.Range.inclusive(portFrom, portTo)) takeWhile
    bind(configureRoutes(system), akkaConfig.getString("akka.http.interface"), akkaConfig.getString("akka.http.path-prefix"))

}
