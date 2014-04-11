import java.util.Date

package object models {

  case class Author(id: Option[Long], name: String)

  case class Tag(id: Option[Long], name: String)

  case class Book(id: Option[Long],
                  isbn10: Option[String],
                  isbn13: Option[String],
                  title: String,
                  authors: List[Author],
                  description: Option[String],
                  publisher: Option[String],
                  datePublished: Option[Date],
                  language: Option[String],
                  pageCount: Option[Int],
                  notes: Option[String],
                  coverPictureUrl: Option[String],
                  tags: List[Tag],
                  dateCreated: Date,
                  dateModified: Date)

  case class SearchResult(query: String, totalResults: Int, page: Int, results: List[Book])

}
