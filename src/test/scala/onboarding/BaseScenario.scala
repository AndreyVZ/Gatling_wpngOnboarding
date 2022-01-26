package onboarding

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import onboarding.Feeders._


object BaseScenario{
  def apply(): ScenarioBuilder = new BaseScenario().scn
}


class BaseScenario {

  val grOB_StudRegPurchase: ChainBuilder = group("grOB_StudRegPurchase"){
    exec(Actions.wpngCstUserCheckLogin)
      .exec(Actions.wpngCstInstitutionSearch).exec(Methods.myPause())
      .exec(Actions.wpngCstUserCreate).exec(Methods.myPause())
      .exec(Actions.wpngCstUserActivateLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstUserSetPassword).exec(Methods.myPause())
      .exec(Actions.wpngSsoUserLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstUserSearchByLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstInstitutionDetails).exec(Methods.myPause())
      .exec(Actions.wpngCstUserEnrollments).exec(Methods.myPause())
      .exec(Actions.wpngCstInstitutionSections).exec(Methods.myPause())
      .exec(Actions.wpngCstSectionEnrollStudent).exec(Methods.myPause())
      .exec(Actions.wpngCstEcommercePurchaseOptions).exec(Methods.myPause())
      .exec(Actions.wpngCstEcommerceLaunchCart).exec(Methods.myPause())
  }

  val grOB_StudRegRegcode: ChainBuilder = group("grOB_StudRegRegcod"){
    exec(Actions.wpngCstUserCheckLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstInstitutionSearch).exec(Methods.myPause())
      .exec(Actions.wpngCstUserCreate).exec(Methods.myPause())
      .exec(Actions.wpngCstUserActivateLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstUserSetPassword).exec(Methods.myPause())
      .exec(Actions.wpngSsoUserLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstUserSearchByLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstInstitutionDetails).exec(Methods.myPause())
      .exec(Actions.wpngCstUserEnrollments).exec(Methods.myPause())
      .exec(Actions.wpngCstInstitutionSections).exec(Methods.myPause())
      .exec(Actions.wpngCstSectionEnrollStudent).exec(Methods.myPause())
      .exec(Actions.wpngCstEcommercePurchaseOptions).exec(Methods.myPause())
      .exec(Actions.wpngCstEcommerceValidateRegCode).exec(Methods.myPause())
      .exec(Actions.wpngCstEcommerceBurnRegCode).exec(Methods.myPause())
      .exec(Actions.wpngCstEcommercePurchaseOptions).exec(Methods.myPause())
  }

  val grOB_StudRegFreeTrial: ChainBuilder = group("grOB_StudRegFreeTrial"){
    exec(Actions.wpngCstUserCheckLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstInstitutionSearch).exec(Methods.myPause())
      .exec(Actions.wpngCstUserCreate).exec(Methods.myPause())
      .exec(Actions.wpngCstUserActivateLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstUserSetPassword).exec(Methods.myPause())
      .exec(Actions.wpngSsoUserLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstUserSearchByLogin).exec(Methods.myPause())
      .exec(Actions.wpngCstInstitutionDetails).exec(Methods.myPause())
      .exec(Actions.wpngCstUserEnrollments).exec(Methods.myPause())
      .exec(Actions.wpngCstInstitutionSections).exec(Methods.myPause())
      .exec(Actions.wpngCstSectionEnrollStudent).exec(Methods.myPause())
      .exec(Actions.wpngCstLicenseFreeTrial).exec(Methods.myPause())
  }

  val scn: ScenarioBuilder = scenario("Common Scenario")
    .feed(obStudentName)
    .feed(obSections)
    .feed(obRegcode)
    .randomSwitch(
      33.33 -> exec(grOB_StudRegPurchase),
      33.33 -> exec(grOB_StudRegRegcode),
      33.34 -> exec(grOB_StudRegFreeTrial)
    )



}
