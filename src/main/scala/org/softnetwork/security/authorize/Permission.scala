package org.softnetwork.security.authorize

import org.softnetwork.security.io._

/**
 * Created by smanciot on 23/07/14.
 */
case class Permission(permission:String) {

  lazy val parts:Seq[TextPart] = splitTextParts(Seq[TextPart]())(permission)

  val evaluate : Parameters => String = f => parts.foldLeft[String]("")(_ + _.evaluate(f))

  val hasPrivilege : Seq[String] => String => Boolean = privileges => search => {
    privileges.contains(search) ||
      privileges.contains(search + ":" + ALL)
  }

  val hasAllPrivileges : Seq[String] => Seq[String] => Boolean = privileges => searchParts => {
    if(searchParts.isEmpty)
      hasPrivilege(privileges)(ALL)
    else
      hasPrivilege(privileges)(searchParts.foldLeft("")(_  + _ + ":") + ALL) || hasAllPrivileges(privileges)(searchParts.dropRight(1))
  }

  val isPermitted:String => Seq[String] => Boolean = {permission => privileges =>
    val permissionParts : Array[String] = permission.split(':')
    hasPrivilege(privileges)(permissionParts.mkString(":")) || hasAllPrivileges(privileges)(permissionParts.toSeq)
  }

  def isPermitted(parameters:Parameters):Seq[String] => Boolean = isPermitted(evaluate(parameters))
}
