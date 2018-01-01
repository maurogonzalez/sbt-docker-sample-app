package com.maurogonzalez.services

import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.concurrent.ExecutionContext

trait BaseService extends FailFastCirceSupport {
  protected implicit def log: LoggingAdapter
  protected implicit def executor: ExecutionContext
  protected def routes(): Route
}
