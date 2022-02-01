package onboarding

import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder

import scala.util.Random

object Feeders {

  val obStudentName = Iterator.continually {
    Map("student_name" -> s"perf_onboard_stud_${Random.alphanumeric.take(20).mkString}")
  }
  val obSections: BatchableFeederBuilder[String] = csv("ob_sections.csv").random.eager //from RAM
  val obRegcode: BatchableFeederBuilder[String] = csv("ob_regcode.csv").random.eager //from RAM
  val obStudLogin: BatchableFeederBuilder[String] = csv("ob_studLogin.csv").random.eager //from RAM


}
