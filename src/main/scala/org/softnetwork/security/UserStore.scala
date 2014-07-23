package org.softnetwork.security

/**
 * Created by smanciot on 23/07/14.
 */
trait UserStore[T]{
  def contains(principal:T):Boolean
  def loadUserRoles(principal:T):Option[Seq[String]]
  def loadUserPrivileges(principal:T):Option[Seq[String]]
  def loadRolePrivileges(role:String):Option[Seq[String]]
}

case class InMemoryStore(
                     var users:Seq[String] = Seq[String](),
                     var roles:Map[String, Seq[String]] = Map[String, Seq[String]](),
                     var userPrivileges:Map[String, Seq[String]] = Map[String, Seq[String]](),
                     var rolePrivileges:Map[String, Seq[String]] = Map[String, Seq[String]]()) extends UserStore[String]
{
  def contains(principal:String):Boolean = users.contains(principal)
  def loadUserRoles(principal:String):Option[Seq[String]] = roles.get(principal)
  def loadUserPrivileges(principal:String):Option[Seq[String]] = userPrivileges.get(principal)
  def loadRolePrivileges(role:String):Option[Seq[String]] = rolePrivileges.get(role)
}


