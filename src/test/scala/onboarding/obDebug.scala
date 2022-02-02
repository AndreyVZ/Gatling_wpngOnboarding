package onboarding

import io.gatling.core.Predef._


class obDebug extends Simulation {


  setUp(
    BaseScenario().inject(atOnceUsers(24))
  ).protocols(
    httpProtocol
  )
    .maxDuration(120000)
    .assertions(global.responseTime.max.lt(10000))

}
