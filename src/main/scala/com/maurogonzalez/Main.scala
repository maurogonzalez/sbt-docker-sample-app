package com.maurogonzalez

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.separateOnSlashes
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.CorsDirectives._
import com.maurogonzalez.services.{NameService, StatusService, SwaggerDocService}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object Main extends App with HttpService with RequestLogging {

  implicit def executor = aS.dispatcher
  protected def log = Logging(aS, service)
  implicit val aS: ActorSystem = ActorSystem()
  implicit val aM: ActorMaterializer = ActorMaterializer()
  override def akkaConfig: Config = ConfigFactory.load()

  protected def configureRoutes(system: ActorSystem): Route = {
    cors()(StatusService().routes() ~ NameService().routes() ~
      SwaggerDocService(aM, akkaConfig.getString("akka.http.path-prefix"), akkaConfig.getString("swagger.host")).routes)
  }

  def bind(route: Route, interface: String, basePath: String): Int ⇒ Boolean =
    (port: Int) ⇒ {
      val mainRouter = pathPrefix(separateOnSlashes(basePath)) { route }

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
    bind(configureRoutes(aS), akkaConfig.getString("akka.http.interface"), akkaConfig.getString("akka.http.path-prefix"))

}
