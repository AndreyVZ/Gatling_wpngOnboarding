package onboarding

import io.gatling.core.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

class obStability extends Simulation {

  val intensity: Int = 2 //TPS
  val stageDuration: FiniteDuration = 10 minute
  val rampDuration: FiniteDuration = 2 minute
  val testDuration: FiniteDuration = 12 minute

  setUp(
    BaseScenario().inject(
      rampUsersPerSec(0) to intensity.toInt during rampDuration, //разгон
      constantUsersPerSec(intensity.toInt) during stageDuration //полка
    )
  ).protocols(httpProtocol)
    .maxDuration(testDuration) //длительность теста = разгон + полка testDuration


}