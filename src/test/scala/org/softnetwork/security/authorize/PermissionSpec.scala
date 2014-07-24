package org.softnetwork.security.authorize

import org.specs2.mutable._

/**
 * Created by smanciot on 24/07/14.
 */
object PermissionSpec extends Specification {

  "Permission" should {
    "evaluate parameters specified at run time" in {
      val p = Permission("permission1:{param1}:{param2}")
      val parameters:Parameters = InMemoryParameters(Map[String, String](("param1","1"), ("param2","2")))
      p.evaluate(parameters) mustEqual "permission1:1:2"
    }
    "not evaluate parameters that have not been specified at run time" in {
      val p = Permission("permission1:{param1}:{param2}")
      val parameters:Parameters = InMemoryParameters(Map[String, String](("param1","1")))
      p.evaluate(parameters) mustEqual "permission1:1:" + ANY
    }
    "check the permission against the privileges that have been granted" in {
      val p = Permission("permission1:{param1}:{param2}")
      val parameters:Parameters = InMemoryParameters(Map[String, String](("param1","1")))
      val f = p.isPermitted(parameters)
      f(Seq[String]()) mustEqual false
      f(Seq[String](ALL)) mustEqual true
      f(Seq[String]("permission1")) mustEqual false
      f(Seq[String]("permission1:" + ALL)) mustEqual true
      f(Seq[String]("permission1:1")) mustEqual false
      f(Seq[String]("permission1:1:" + ALL)) mustEqual true
      // FIXME f(Seq[String]("permission1:1:1")) mustEqual true
    }
  }

}
