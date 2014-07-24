package org.softnetwork.security.authorize

import com.typesafe.config.ConfigFactory
import org.softnetwork.security.InMemoryStore
import org.specs2.mutable._
import spray.http._
import spray.routing._
import spray.routing.authentication._
import spray.testkit.Specs2RouteTest

/**
 * Created by smanciot on 24/07/14.
 */
object SprayAuthorizeWithAccessControlSpec extends Specification with Specs2RouteTest with HttpService with TestEnvironment{
  def actorRefFactory = system

  def extractUser(userPass: UserPass): String = userPass.user
  val config = ConfigFactory.parseString("John = p4ssw0rd\nPeter = pan")
  val store:InMemoryStore = userStore.asInstanceOf[InMemoryStore]
  store.users +:= "Peter"
  store.roles += "Peter" -> Seq[String]("r1", "r2")
  store.userPrivileges += "Peter" -> Seq[String]("p0","p1:*", "p2:subp2")
  val hasPermissionToPetersLair:String => Boolean = (role("r1") && permission("p0"))()

  val route =
    sealRoute {
      authenticate(BasicAuth(realm = "secure site", config = config, createUser = extractUser _)) { userName =>
        path("peters-lair") {
          authorize(hasPermissionToPetersLair(userName)) {
            complete(s"'$userName' visited Peter's lair")
          }
        }
      }
    }

  val johnsCred = BasicHttpCredentials("John", "p4ssw0rd")
  "unauthorized" in{
    Get("/peters-lair") ~>
      addCredentials(johnsCred) ~> route ~> check {
      status === StatusCodes.Forbidden
      responseAs[String] === "The supplied authentication is not authorized to access this resource"
    }
  }

  val petersCred = BasicHttpCredentials("Peter", "pan")
  "authorized" in {
    Get("/peters-lair") ~>
      addCredentials(petersCred) ~> route ~> check {
      responseAs[String] === "'Peter' visited Peter's lair"
    }
  }

}
