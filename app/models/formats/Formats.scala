package models.formats

import play.api.libs.json._
import models.{Author, Defaults, Book}
import play.api.libs.json.JsString
import java.text.SimpleDateFormat
import anorm.NotAssigned
import java.util.Date

object Formats {
  implicit object BookFormat extends Reads[Book] {

    def reads(volumeInfo: JsValue): Book = {
      val title = (volumeInfo \ "title").asOpt[String]
      val authors = (volumeInfo \ "authors").asOpt[List[String]]
      val description = (volumeInfo \ "description").asOpt[String]

      val publisher = (volumeInfo \ "publisher").asOpt[String]
      val datePublished = (volumeInfo \ "publishedDate") match {
        case JsString(str) if str.length == Defaults.GoogleBooksDateFormat.length => Some(new SimpleDateFormat(Defaults.GoogleBooksDateFormat).parse(str))
        case JsString(str) if str.length == Defaults.GoogleBooksShortDateFormat.length => Some(new SimpleDateFormat(Defaults.GoogleBooksShortDateFormat).parse(str))
        case _ => None
      }
      val language = (volumeInfo \ "language").asOpt[String]
      val imgUrl = (volumeInfo \ "imageLinks" \ "thumbnail").asOpt[String]
      val pageCount = (volumeInfo \ "pageCount").asOpt[Int]

      val isbn10 = (volumeInfo \ "industryIdentifiers")(0).\("identifier").asOpt[String]
      val isbn13 = (volumeInfo \ "industryIdentifiers")(1).\("identifier").asOpt[String]

      Book(NotAssigned, isbn10, isbn13, title.getOrElse(""), authors.map{_.map{name => Author(NotAssigned, name)}}.getOrElse(List()),
        description, publisher, datePublished, language, pageCount, None, imgUrl, List(), new Date(), new Date())

    }
  }
}