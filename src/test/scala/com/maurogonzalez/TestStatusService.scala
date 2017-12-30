package com.maurogonzalez

import akka.http.scaladsl.model.StatusCodes
import com.maurogonzalez.models.Status
import com.maurogonzalez.services.StatusService


class TestStatusService extends TestBaseService with StatusService {
  import io.circe.generic.auto._


  "StatusService" when {
    "GET /status" should {
      "return time" in {
        Get("/status") ~> routes() ~> check {
          status should be(StatusCodes.OK)
          responseAs[Status].uptime should include("milliseconds")
        }
      }
    }
  }

}
