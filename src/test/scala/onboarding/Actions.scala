package onboarding

import io.gatling.core.Predef._
import io.gatling.http.Predef._


object Actions {

  //===== Student Registration ======================

  //wpng_onboard_stud_registration_purchase

  def wpngCstUserCheckLogin = {
    exec(
      http("WPNG_CST_User_Check_Login")
        .get("/wpng/api/v1/users/checkLoginName?loginName=${student_name}@wpng.com")
        .check(status is 200)
        .check(jsonPath("$.used").saveAs("used")).check()

    )
      .doIf(session => session("used").as[Boolean]) {
        println("!!! User is already registered !!!")
        exitHere
      }


  }

  def wpngCstInstitutionSearch = {
    exec(
      http("WPNG_CST_Institution_Search")
        .get("/wpng/api/v1/institutions/search?name=${institution_name}")
        .check(status is 200)
    )
  }

  def wpngCstUserCreate = {
    exec(
      http("WPNG_CST_User_Create")
        .post("/wpng/api/v1/users/student")
        .body(StringBody("""
                           |{
                           |"firstName":"${student_name}",
                           |"lastName":"${student_name}",
                           |"userRole":"STUDENT",
                           |"loginName":"${student_name}@wpng.com",
                           |"studentId":"",
                           |"graduationYear":"2021",
                           |"subscribeWileyAlerts":false,
                           |"learningStyleSurveyDeliveryForm":"PRINT",
                           |"learningStyleSurveyDeliveryPlatform":"PC",
                           |"acceptedTermsOfService":true,
                           |"institutions":[${institution_id}],
                           |"captchaToken":"10000000-aaaa-bbbb-cccc-000000000001"
                           |}
                           |""".stripMargin)).asJson
        .check(status is 200)
        .check(jsonPath("$.userLogins[0].id").saveAs("user_login_id"))
        .check(jsonPath("$.id").saveAs("user_profile_id"))
    )
//    .exec(session => {
//      println(">>>>user_login_id=" + session("user_login_id").as[String])
//      session})
//      .exec(
//        println(""">>>>user_login_id='${user_login_id}'""")
//        )

  }

  def wpngCstUserActivateLogin = {
    exec{
      session => session.set("activation_link_guid",
        Methods.getActivationLinkGuide("SELECT activation_link_guid FROM login_activation WHERE user_login_id = '" +session("user_login_id").as[String] + "'"))
    }
    .exec(
      http("WPNG_CST_User_Activate_Login")
        .get("/wpng/api/v1/users/activateLogin?guid=${activation_link_guid}")
        .check(status is 200)
    )
  }

  def wpngCstUserSetPassword = {
    exec(
      http("WPNG_CST_User_Set_Password")
        .put("/wpng/api/v1/users/setPassword")
        .body(StringBody(
          """
            |{
            |"operation":"set_password",
            |"login_activation_guid":"${activation_link_guid}",
            |"wileyId":"${student_name}@wpng.com",
            |"password":"stresstest"
            |}
            |""".stripMargin)).asJson
        .check(status is 200)
      //.check(jsonPath("$.userLogins[0].id").saveAs("user_login_id"))
    )
  }

  def wpngSsoUserLogin = {
    exec(
      http("WPNG_SSO_User_Login")
        .post("/wpng/api/v1/sso/jwt/login")
        .body(StringBody("userid=${student_name}@wpng.com&password=stresstest"))
        .header("content-type", "application/x-www-form-urlencoded")
        .check(status is 200)
    )
  }

  def wpngCstUserSearchByLogin = {
    exec(
      http("WPNG_CST_User_Search_by_Login")
        .get("/wpng/api/v1/users?loginName=${student_name}@wpng.com")
        .check(status is 200)
        .check(jsonPath("$.profileKey").saveAs("user_profile_key"))
    )
  }

  def wpngCstInstitutionDetails = {
    exec(
      http("WPNG_CST_Institution_Details")
        .get("/wpng/api/v1/institutions?userProfileKey=${user_profile_key}&role=STUDENT")
        .check(status is 200)
    )
  }

  def wpngCstUserEnrollments = {
    exec(
      http("WPNG_CST_User_Enrollments")
        .get("/wpng/api/v1/users/${user_profile_id}/enrollments")
        .check(status is 200)
    )
  }

  def wpngCstInstitutionSections = {
    exec(
      http("WPNG_CST_Institution_Sections")
        .get("/wpng/api/v1/institutions/${institution_id}/coursesections/search")
        .check(status is 200)
    )
  }

  def wpngCstSectionEnrollStudent = {
    exec(
      http("WPNG_CST_Section_Enroll_Student")
        .post("/wpng/api/v1/coursesections/${section_id}/enroll?userId=${user_profile_id}&role=STUDENT")
        .header("content-type", "application/x-www-form-urlencoded")
        .check(status is 200)
    )
  }

  def wpngCstEcommercePurchaseOptions = {
    exec(
      http("WPNG_CST_Ecommerce_Purchase_Options")
        .post("/wpng/api/v1/ecommerce/purchaseoptions")
        .header("Cache-Control", "max-age=0")
        .body(StringBody(
          """
            |{
            |"courseSectionId":"${section_id}",
            |"productId":"${product_id}",
            |"courseType":"WILEY"
            |}
            |""".stripMargin)).asJson
        .check(status is 200)
        .check(jsonPath("$.wpSemesterType[0].instantAccessISBN").saveAs("instantAccessISBN"))
        .check(jsonPath("$.wpSemesterType[0].purchaseType").saveAs("purchaseType"))
    )
  }

  def wpngCstEcommerceLaunchCart = {
    exec(
      http("WPNG_CST_Ecommerce_Launch_Cart")
        .post("/wpng/api/v1/ecommerce/launchcart")
        .body(StringBody(
          """
            |{
            |"courseSectionId":"${section_id}",
            |"courseType":"WILEY",
            |"productId":"${product_id}",
            |"purchaseISBN":"${instantAccessISBN}",
            |"purchaseType":"${purchaseType}",
            |"lmsType":"JWSCANVAS",
            |"backReturnUrl":"https://education-perf.wiley.com/ngonboard/index.html#/RegisterCourse?courseSectionId=${section_id}&user_profile_id=${user_profile_id}&course_type=WILEY",
            |"successReturnUrl":"https://wiley2.instructure.com/courses/48148"
            |}
            |""".stripMargin)).asJson
        .check(status is 200)
        //.check(jsonPath("$.message").saveAs("message"))
    )
  }

  //wpng_onboard_stud_registration_free_trial

  def wpngCstLicenseFreeTrial = {
    exec(
      http("WPNG_CST_License_Free_Trial")
        .post("/wpng/api/v1/licenses/trial")
        .body(StringBody(
          """
            |{
            |"courseSectionId":"${section_id}",
            |"courseType":"WILEY",
            |"licenseType":"SUBSCRIPTION",
            |"licenseModel":"TRIAL",
            |"userProfileId":"${user_profile_id}",
            |"productId":"${product_id}"
            |}
            |""".stripMargin)).asJson
        .check(status is 200)
    )
  }

  //wpng_onboard_stud_registration_reg_code

  def wpngCstEcommerceValidateRegCode = {
    exec(
      http("WPNG_CST_Ecommerce_Validate_Reg_Code")
        .post("/wpng/api/v1/ecommerce/regcodes/validate")
        .body(StringBody(
          """
            |{
            |"courseSectionId":"${section_id}",
            |"courseType":"WILEY",
            |"lmsType":"JWSCANVAS",
            |"productId":"${product_id}",
            |"regCode":"${regcode}",
            |"userProfileId":"${user_profile_id}"
            |}
            |""".stripMargin)).asJson
        .check(status is 200)
    )
  }

  def wpngCstEcommerceBurnRegCode = {
    exec(
      http("WPNG_CST_Ecommerce_Burn_Reg_Code")
        .post("/wpng/api/v1/ecommerce/regcodes/burn")
        .body(StringBody(
          """
            |{
            |"courseSectionId":"${section_id}",
            |"courseType":"WILEY",
            |"lmsType":"JWSCANVAS",
            |"productId":"${product_id}",
            |"regCode":"${regcode}",
            |"userProfileId":"${user_profile_id}"
            |}
            |""".stripMargin)).asJson
        .check(status is 200)
    )
  }


}
