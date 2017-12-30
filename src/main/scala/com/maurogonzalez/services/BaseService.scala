package com.maurogonzalez.services

import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

trait BaseService extends BaseComponent with FailFastCirceSupport {
  protected def routes(): Route
}
