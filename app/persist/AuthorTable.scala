package persist

import scala.slick.lifted
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.StaticQuery.interpolation
import persist.BookTable.DbBookId

object AuthorTable {


  class DbAuthorId(val id: Long) extends AnyVal

  implicit val dbAuthorIdType = MappedColumnType.base[DbAuthorId, Long](_.id, new DbAuthorId(_))

  case class DbAuthor(id: DbAuthorId, name: String)

  class Authors(tag: lifted.Tag) extends Table[DbAuthor](tag, "author") {
    def id = column[DbAuthorId]("tag_id", O.PrimaryKey)

    def name = column[String]("name")

    override def * = (id, name) <>(DbAuthor.tupled, DbAuthor.unapply)
  }

  object authors extends TableQuery(new Authors(_)) {

    val byName = this.findBy(_.name)

    def byBookId(bookId: Column[DbBookId]) = this innerJoin BookAuthorTable.bookAuthors where(_._2.bookId === bookId) map(_._1)

    def cleanup = sqlu"""delete from author where author_id not in (select distinct author_id from book_author)"""
  }

}
