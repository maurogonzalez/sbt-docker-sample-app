package com.maurogonzalez

import com.maurogonzalez.models.Name
import com.maurogonzalez.services.NameService
import io.circe.generic.auto._

class TestMauroService extends TestBaseService {

  "CalculatorService" when {

    "Post /name with Mauro" should {
      "return Mauro as name " in {
        Post("/name", Name("Mauro")) ~> NameService().routes() ~> check {
          responseAs[Name]
            .name should be("Sample-Mauro")
        }
      }
    }
  }
}
