package models

import anorm.{NotAssigned, Id, Pk}

/**
 * @author Lukas Sembera <semberal@gmail.com>
 */
object Defaults {
  val GoogleBooksDateFormat = "yyyy-MM-dd"
  val GoogleBooksShortDateFormat = "yyyy"

  /*
   * Implicit conversion from Option[T] to Pk[T] (because of the annoying executeInsert() returning Option[Long]
   */
  implicit def optionToPk[T](opt: Option[T]): Pk[T] = opt match {
    case Some(x) => Id(x)
    case None => NotAssigned
  }

}
