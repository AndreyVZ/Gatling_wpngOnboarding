package onboarding

import io.gatling.core.Predef._


class obDebug extends Simulation {


  setUp(
    BaseScenario().inject(atOnceUsers(9))
  ).protocols(
    httpProtocol
  )
    .maxDuration(60000)
    .assertions(global.responseTime.max.lt(10000))

}
