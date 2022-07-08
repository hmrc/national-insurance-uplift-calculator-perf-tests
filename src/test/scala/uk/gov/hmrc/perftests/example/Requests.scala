/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.example

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object Requests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("national-insurance-uplift-calculator-frontend")
  val route: String   = "/estimate-how-national-insurance-contributions-changes-affect-you"

  val navigateToHomePage: HttpRequestBuilder =
    http("Navigate to Home Page")
      .get(s"$baseUrl$route/")
      .check(status.is(200))

  val getAnnualSalaryPage: HttpRequestBuilder =
    http("Get Annual Salary Page")
      .get(s"$baseUrl$${salaryPage}")
      .check(status.is(200))
      .check(header("Location").is(s"$route/annual-salary").saveAs("salaryPage"))
      .check(css("input[name=csrfToken]", "value").saveAs("csrfToken"))

  val postAnnualSalaryPage: HttpRequestBuilder =
    http("Post Annual Salary Page")
      .post(s"$baseUrl$${resultPage}")
      .formParam("value", "25000")
      .formParam("csrfToken", s"$${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"$route/estimated-change").saveAs("resultPage"))

  val getResultPage: HttpRequestBuilder =
    http("Get Result Page")
      .get(s"$baseUrl$${resultPage}")
      .check(status.is(200))
}
