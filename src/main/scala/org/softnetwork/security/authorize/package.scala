package org.softnetwork.security

/**
 * Created by smanciot on 23/07/14.
 */
package object authorize {

  val ALL = "*"

  val ANY = "?"

  type Parameters = org.softnetwork.security.io.Parameters
}

package authorize {

  case class InMemoryParameters(var parameters:Map[String, String]=Map[String, String]()) extends Parameters{
    def apply(key:String):Option[String] = parameters.get(key)
  }

}