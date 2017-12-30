package com.maurogonzalez

import akka.event.{LoggingAdapter, NoLogging}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

trait TestBaseService extends WordSpec with Matchers with ScalatestRouteTest with MockFactory {
  protected def log: LoggingAdapter = NoLogging
}