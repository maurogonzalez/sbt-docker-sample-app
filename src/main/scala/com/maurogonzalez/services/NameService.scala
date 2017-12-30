package com.maurogonzalez.services

import javax.ws.rs.Path

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.maurogonzalez.RequestLogging
import org.slf4j.LoggerFactory
import com.maurogonzalez.models.Name
import io.circe.Json
import io.circe.generic.auto._
import io.swagger.annotations._

import scala.concurrent.Future
import scala.util.{Failure, Success}

@Path("/name")
@Api(value = "/name", produces = "application/json", consumes = "application/json" )
trait NameService extends BaseService with RequestLogging {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def nameProcessor(name: Name): Future[Name] = Future {
    Name(s"Sample-${name.name}")
  }

  @ApiOperation(value = "Return well-formed json", notes = "", httpMethod = "POST",
    nickname = "name", produces = "application/json", consumes = "application/json", response = classOf[Name])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "name", paramType = "body", dataTypeClass = classOf[Name])
  ))
  override protected def routes(): Route =
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
