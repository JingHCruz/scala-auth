package org.softnetwork.security.authorize

import org.softnetwork.security.InMemoryStore
import org.specs2.mutable._

/**
 * Created by smanciot on 24/07/14.
 */
object AccessControlSpec extends Specification with TestEnvironment{

  "AccessControl" should {
    "check" in {
      "roles" in {
        val principal = "sample"
        val store:InMemoryStore = userStore.asInstanceOf[InMemoryStore]
        store.users +:= principal
        store.roles += principal -> Seq[String]("r1", "r2")
        role("fake")(principal) mustEqual false
        role("r1")(principal) mustEqual true
        role("r1")("fake") mustEqual false
        (role("r1") && role("r2"))(principal) mustEqual true
        (!role("fake") && role("r2"))(principal) mustEqual true
        (role("fake") || role("r1"))(principal) mustEqual true
      }
      "privileges" in {
        val principal = "sample"
        val store:InMemoryStore = userStore.asInstanceOf[InMemoryStore]
        store.users +:= principal
        store.userPrivileges += principal -> Seq[String]("p0","p1:*", "p2:subp2")
        val params:InMemoryParameters = parameters.asInstanceOf[InMemoryParameters]
        params.parameters += "param" -> "subp2"
        permission("fake")(principal) mustEqual false
        permission("p0")(principal) mustEqual true
        permission("p0:x")(principal) mustEqual false
        permission("p0")("fake") mustEqual false
        (permission("p0") && permission("p1:x"))(principal) mustEqual true
        (!permission("fake") && permission("p2:subp2"))(principal) mustEqual true
        (permission("fake") || permission("p2:{param}"))(principal) mustEqual true
      }
      "roles and privileges" in {
        val principal = "sample"
        val store:InMemoryStore = userStore.asInstanceOf[InMemoryStore]
        store.users +:= principal
        store.roles += principal -> Seq[String]("r1", "r2")
        store.userPrivileges += principal -> Seq[String]("p0","p1:*", "p2:subp2")
        val params:InMemoryParameters = parameters.asInstanceOf[InMemoryParameters]
        params.parameters += "param" -> "subp2"
        (role("fake") && permission("fake"))(principal) mustEqual false
        !(role("fake") && permission("fake"))(principal) mustEqual true
        (role("r1") && permission("p0"))(principal) mustEqual true
        (role("r1") && permission("p0"))("fake") mustEqual false
        ((!role("fake") && role("r2")) && (!permission("fake") && permission("p2:{param}")))(principal) mustEqual true
      }
    }
  }

}
