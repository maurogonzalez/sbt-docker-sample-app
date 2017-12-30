package com.maurogonzalez

import akka.actor.ActorSystem
import akka.http.scaladsl.server._
import akka.stream.Materializer
import ch.megard.akka.http.cors.CorsDirectives._
import com.maurogonzalez.services.{NameService, StatusService}
import com.typesafe.config.Config

import scala.concurrent.ExecutionContextExecutor

trait HttpService extends RouteConcatenation with StatusService with NameService {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def akkaConfig: Config

  protected def configureRoutes(system: ActorSystem): Route = {
    cors()(super[StatusService].routes() ~ super[NameService].routes())
  }
}
