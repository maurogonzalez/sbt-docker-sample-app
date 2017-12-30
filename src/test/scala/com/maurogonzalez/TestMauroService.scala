package com.maurogonzalez

import com.maurogonzalez.models.Name
import com.maurogonzalez.services.NameService

class TestMauroService extends TestBaseService with NameService {
  import io.circe.generic.auto._

  "CalculatorService" when {

    "Post /name with Mauro" should {
      "return Mauro as name " in {
        Post("/name", Name("Mauro")) ~> routes() ~> check {
          responseAs[Name]
            .name should be("Sample-Mauro")
        }
      }
    }
  }
}
