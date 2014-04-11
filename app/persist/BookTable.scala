package persist

import scala.slick.lifted
import scala.slick.driver.H2Driver.simple._
import java.sql.Date
import models.{Tag, Author, Book}
import persist.AuthorTable._
import persist.TagTable._

object BookTable {

  class DbBookId(val id: Long) extends AnyVal

  implicit val dbBookIdType = MappedColumnType.base[DbBookId, Long](_.id, new DbBookId(_))

  case class DbBook(id: DbBookId, isbn10: Option[String], isbn13: Option[String],
                    title: String, description: Option[String], publisher: Option[String],
                    datePublished: Option[Date], language: Option[String], pageCount: Option[Int],
                    notes: Option[String], coverPictureUrl: Option[String],
                    dateCreated: Date, dateModified: Date)

  class Books(tag: lifted.Tag) extends Table[DbBook](tag, "book") {
    def id = column[DbBookId]("book_id", O.PrimaryKey)

    def isbn10 = column[String]("isbn10", O.Nullable)

    def isbn13 = column[String]("isbn13", O.Nullable)

    def title = column[String]("title")

    def description = column[String]("description", O.Nullable)

    def publisher = column[String]("publisher", O.Nullable)

    def datePublished = column[Date]("date_published", O.Nullable)

    def language = column[String]("lang", O.Nullable)

    def pageCount = column[Int]("page_count", O.Nullable)

    def notes = column[String]("notes", O.Nullable)

    def coverPictureUrl = column[String]("cover_picture_url", O.Nullable)

    def dateCreated = column[Date]("date_created")

    def dateModified = column[Date]("date_modified")

    override def * =
      (id, isbn10.?, isbn13.?, title, description.?,
        publisher.?, datePublished.?, language.?, pageCount.?, notes.?,
        coverPictureUrl.?, dateCreated, dateModified) <>(DbBook.tupled, DbBook.unapply)
  }

  def books = new TableQuery(new Books(_)) {

    def getAllBooks()(implicit session: Session) =
      this.list().map(book =>
        Book(Some(book.id.id), book.isbn10, book.isbn13, book.title, {authors.byBookId(book.id).list().map(a => Author(Some(a.id.id), a.name))}, book.description,
          book.publisher, book.datePublished, book.language, book.pageCount, book.notes, book.coverPictureUrl, {tags.byBookId(book.id).list().map(t => Tag(Some(t.id.id), t.name))},
          book.dateCreated, book.dateModified)
      )

    def getAllBooksAlternative = for {
      ((b, _), a) <- this leftJoin BookAuthorTable.bookAuthors on (_.id === _.bookId) leftJoin authors on(_._2.authorId === _.id)
    } yield {
      // todo https://groups.google.com/forum/#!starred/scalaquery/2svT8c2M_d4


    }

  }

}
