package persist

import scala.slick.lifted
import scala.slick.driver.H2Driver.simple._
import persist.BookTable.{books, DbBookId}
import persist.TagTable.{tags, DbTagId}

object BookTagTable {

  class BookTag(tag: lifted.Tag) extends Table[(DbBookId, DbTagId)](tag, "book_tag") {
    def bookId = column[DbBookId]("book_id")
    def tagId = column[DbTagId]("tag_id")

    def * = (bookId, tagId)

    def bookFk = foreignKey("book_fk", bookId, books)(_.id)
    def authorFk = foreignKey("tag_fk", tagId, tags)(_.id)
  }

  val bookTags = TableQuery[BookTag]
}
