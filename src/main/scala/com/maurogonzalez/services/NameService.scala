package com.maurogonzalez.services

import javax.ws.rs.Path

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.maurogonzalez.RequestLogging
import com.maurogonzalez.models.Name
import io.circe.generic.auto._
import io.swagger.annotations._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Path("/name")
@Api(value = "/name", produces = "application/json", consumes = "application/json" )
case class NameService()(implicit system: ActorSystem) extends BaseService with RequestLogging {
  implicit def executor: ExecutionContext = system.dispatcher
  def log = Logging(system, "name-service")

  def nameProcessor(n: Name): Future[Name] = Future {
    Name(s"Sample-${n.name}")
  }

  @ApiOperation(value = "Return your Sample name", notes = "Sample name", httpMethod = "POST",
    nickname = "name", produces = "application/json", consumes = "application/json", response = classOf[Name])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "name", paramType = "body", dataTypeClass = classOf[Name])
  ))
  def routes(): Route =
    path("name") {
        logRequestResult(loggingMagnet) {
          post {
            entity(as[Name]) { (name: Name) =>
              onComplete({
                nameProcessor(name)
              }) {
                case Success(nameProcessed) =>
                  complete(nameProcessed)
                case Failure(t) => t match {
                  case _ => complete(StatusCodes.InternalServerError, t)
                }
              }
            }
          }
        }
    }
}
