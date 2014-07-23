package org.softnetwork.security.authorize

import org.softnetwork.security._
import org.softnetwork.security.io._
import org.softnetwork.security.util._

/**
 * Created by smanciot on 23/07/14.
 */

class AccessControl[T](val accessRule:Filter[T]) {
  def &&(other:AccessControl[T]):AccessControl[T] = apply(every(accessRule, other.accessRule))
  def ||(other:AccessControl[T]):AccessControl[T] = apply(any(accessRule, other.accessRule))
  def unary_! = apply(complement(accessRule))
  def apply(principal:T):Boolean = accessRule(principal)
  def apply(f:Filter[T]) : AccessControl[T] = new AccessControl[T](f)
}

trait UserStoreComponent[T]{
  val userStore : UserStore[T]
}

trait ParametersComponent{
  val parameters : Parameters
}

trait AccessControlComponent[T] {this:UserStoreComponent[T] with ParametersComponent =>

  def role(role:String):AccessControl[T] = new AccessControl[T](principal => userStore.loadUserRoles(principal) match {
    case Some(s) => s.contains(role)
    case None => false
  })

  def permission(permission:String):AccessControl[T] = {
    val p = new Permission(permission)
    new AccessControl[T](principal => {
      val isPermitted:Seq[String] => Boolean = p.isPermitted(parameters)
      isPermitted(userStore.loadUserPrivileges(principal) getOrElse Seq[String]())
    })
  }
}

trait TestEnvironment extends AccessControlComponent[String] with UserStoreComponent[String] with ParametersComponent{
  val userStore : UserStore[String] = new InMemoryStore()
  val parameters : Parameters = Map[String, String]()
}
