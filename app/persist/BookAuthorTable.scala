package persist

import scala.slick.lifted
import scala.slick.driver.H2Driver.simple._
import persist.BookTable.{DbBookId, books}
import persist.AuthorTable.{DbAuthorId, authors}

object BookAuthorTable {

  class BookAuthor(tag: lifted.Tag) extends Table[(DbBookId, DbAuthorId)](tag, "book_author") {
    def bookId = column[DbBookId]("book_id")
    def authorId = column[DbAuthorId]("author_id")

    def * = (bookId, authorId)

    def bookFk = foreignKey("book_fk", bookId, books)(_.id)
    def authorFk = foreignKey("author_fk", authorId, authors)(_.id)
  }

  val bookAuthors = new TableQuery(new BookAuthor(_))

}
