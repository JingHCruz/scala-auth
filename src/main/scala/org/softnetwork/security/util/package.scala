package org.softnetwork.security

/**
 * Created by smanciot on 23/07/14.
 */
package object util {

  /**
   * takes a predicate A => Boolean and returns a new function that always returns the opposite of the given predicate
   */
  def complement[A](predicate: A => Boolean) = (a: A) => !predicate(a)

  /**
   * returns a new predicate that, when called with an input a, checks if at least one of its predicates holds true
   * for the value a.
   */
  def any[A](predicates: (A => Boolean)*): A => Boolean = (a:A) => predicates.exists(_(a))

  /**
   * returns the complement of the predicate returned by any – if at least one predicate holds true, the condition
   * for none is not satisfied.
   */
  def none[A](predicates: (A => Boolean)*) = complement(any(predicates: _*))

  /**
   * checks that none of the complements to the predicates passed to it holds true.
   */
  def every[A](predicates: (A => Boolean)*) = none(predicates.view.map(complement(_)): _*)

}
