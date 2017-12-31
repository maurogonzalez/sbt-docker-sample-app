package com.maurogonzalez.services

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.github.swagger.akka._
import com.github.swagger.akka.model.Info
import com.maurogonzalez.RequestLogging
import io.swagger.models.Scheme

import scala.reflect.runtime.{universe => ru}

case class SwaggerDocService(address: String, port: Int, system: ActorSystem, _basePath: String, _host: String) extends SwaggerHttpService with HasActorSystem with RequestLogging {
  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val apiTypes = Seq(ru.typeOf[NameService], ru.typeOf[StatusService])
  override val basePath = _basePath
  override val host = _host
  override val apiDocsPath = "api-docs"
  override val info = Info(description = "Sample App", version = "v1.0.0",
    title = "Sample App", termsOfService = "")
  override val scheme: Scheme = Scheme.HTTP
}
