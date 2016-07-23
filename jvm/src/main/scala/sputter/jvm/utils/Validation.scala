package sputter.jvm.utils

import scalaz.Scalaz._
import scalaz._

/**
 * Simple validation library that can be used to make sure values conform to
 * expected constraints (e.g. are certain lengths, between certain values, etc.)
 */
object Validation {

  /**
   * Returns success if the predicate is true, or the error message as a
   * failure otherwise.
   *
   * @param predicate
   * @param errorMessage
   * @return
   */
  def requireThat(predicate: Boolean)(errorMessage: String): ValidationNel[String, Boolean] = {

    if (predicate) true.successNel[String] else errorMessage.failureNel[Boolean]
  }

  /**
   * Reduces a list of ValidationNels to a single ValidationNel list which
   * either contains all failures as elements, or a single success.
   *
   * @param seq
   * @tparam A
   * @tparam B
   * @return
   */
  def reduceValidationFailures[A, B](seq: Seq[ValidationNel[A, B]]) = {

    implicit val useLast = Semigroup.lastSemigroup[B]
    seq reduceLeft (_ +++ _)
  }
}
