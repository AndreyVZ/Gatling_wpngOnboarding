package onboarding

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

import java.sql.{Connection, DriverManager}



object Methods {

  def myPause() = {
    pause(300 millisecond, 700 millisecond)
  }

  def getActivationLinkGuide(sqlQuery: String): String = {
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://wpng-perf-db.aws.wiley.com/wpng"
    val username = "wpng"
    val password = "Sun_2Rise"
    var activLinkGuid: String = ""

    // there's probably a better way to do this
    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(sqlQuery)
      while ( resultSet.next() ) {
        activLinkGuid = resultSet.getString("activation_link_guid")
        println("activation_link_guid = " + activLinkGuid)
      }
    } catch {
      case e: Throwable => e.printStackTrace
    }
    connection.close()
    return activLinkGuid
  }

}