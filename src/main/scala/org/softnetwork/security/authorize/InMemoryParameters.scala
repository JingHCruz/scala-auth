package org.softnetwork.security.authorize

/**
 * Created by smanciot on 24/07/14.
 */
case class InMemoryParameters(var parameters:Map[String, String]=Map[String, String]()) extends Parameters{
  def apply(key:String):String = parameters.getOrElse(key, "")
}
