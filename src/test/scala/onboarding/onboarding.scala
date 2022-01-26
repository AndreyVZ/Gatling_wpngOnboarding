import io.gatling.core.Predef._
import io.gatling.http.Predef._


package object onboarding {

  val httpProtocol = http
    .proxy(Proxy("localhost", 8888).httpsPort(8888))
    .baseUrl("https://education-perf.wiley.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9") // Here are the common headers
    .header("Cache-Control", "max-age=0")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    //.disableFollowRedirect
}
