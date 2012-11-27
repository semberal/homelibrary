package models.formats

import play.api.libs.json._
import models.{Author, Defaults, Book}
import play.api.libs.json.JsArray
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import java.text.SimpleDateFormat
import anorm.NotAssigned

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
        description, publisher, datePublished, language, pageCount, None, imgUrl, List())

    }

//    def writes(book: Book): JsValue = JsObject(List(
//      "id" -> JsNumber(book.id.getOrElse[Int](0)),
//      "title" -> JsString(book.title),
//      "authors" -> JsArray(book.authors.map(JsString(_))),
//      "description" -> JsString(book.description.getOrElse("")),
//
//      "publisher" -> JsString(book.publisher.getOrElse("")),
////      "datePublished" -> JsNumber(book.datePublished.getOrElse[Int](-1)),
//      "language" -> JsString(book.language.getOrElse("")),
//      "imgUrl" -> JsString(book.coverPictureUrl.getOrElse("")),
//      "pageCount" -> JsNumber(book.pageCount.getOrElse[Int](-1)),
//
//      "isbn10" -> JsString(book.isbn10.getOrElse("")),
//      "isbn30" -> JsString(book.isbn13.getOrElse(""))
//
//    ))
  }
}