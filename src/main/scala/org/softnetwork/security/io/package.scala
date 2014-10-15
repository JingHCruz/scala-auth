package org.softnetwork.security

/**
 * Created by smanciot on 23/07/14.
 */
package object io {

  type Parameters = String => Option[String]

  /**
   * takes longest prefix of characters that is different to the character specified
   */
  val takeUpTo : Char => String => String = c => text => text.takeWhile(_ != c)

  def nextTextPart(text:String): Option[(TextPart, String)] = {
    if(text.trim.length == 0)
      None
    else{
      text.indexOf('{') match{
        case -1 => Some((TextPart(Text, text), ""))
        case 0 => {
          val content = takeUpTo('}')(text.substring(1))
          Some((TextPart(Param, content), text.substring(content.length +2)))
        }
        case _ => {
          val content = takeUpTo('{')(text)
          Some((TextPart(Text, content), text.substring(content.length)))
        }
      }
    }
  }

  val splitTextParts : Seq[TextPart] => String => Seq[TextPart] = parts => text => nextTextPart(text) match{
    case Some((a, b)) => splitTextParts(parts :+ a)(b)
    case None => parts
  }

}

package io {

  sealed trait TextPartType

  case object Param extends TextPartType
  case object Text extends TextPartType

  case class TextPart(partType:TextPartType, content:String){
    val evaluate : Parameters => String = f => partType match{
      case Text => content
      case Param => f(content) match {
        case Some(s) => s
        case None => "?"
      }
    }
  }

}