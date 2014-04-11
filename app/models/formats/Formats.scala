package models.formats

import models.{Author, Book}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.Date

object Formats {
  implicit val bookReads: Reads[Book] = (
    (__ \ "title").readNullable[String] ~
      (__ \ "authors").readNullable[List[String]] ~
      (__ \ "description").readNullable[String] ~
      (__ \ "publisher").readNullable[String] ~
      (__ \ "publishedDate").lazyReadNullable[Date](Reads.dateReads("yyyy-MM-dd") or Reads.dateReads("yyyy")) ~
      (__ \ "language").readNullable[String] ~
      (__ \ "imageLinks" \ "thumbnail").readNullable[String] ~
      (__ \ "pageCount").readNullable[Int] ~
      ((__ \ "industryIdentifiers")(0) \ "identifier").readNullable[String] ~
      ((__ \ "industryIdentifiers")(1) \ "identifier").readNullable[String]
    ).tupled.map {
    case (title, authors, description, publisher, publishedDate, language, thumbnail, pageCount, isbn10, isbn13) =>
      Book(None, isbn10, isbn13, title.getOrElse(""), authors.map(_.map(name => Author(None, name))).getOrElse(List()),
        description, publisher, publishedDate, language, pageCount, None, thumbnail, List(), new Date(), new Date())
  }
}