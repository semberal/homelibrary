package persist

import scala.slick.lifted
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.StaticQuery.interpolation
import persist.BookTable.DbBookId

object TagTable {

  class DbTagId(val id: Long) extends AnyVal

  implicit val dbTagIdType = MappedColumnType.base[DbTagId, Long](_.id, new DbTagId(_))

  case class DbTag(id: DbTagId, name: String)

  class Tags(tag: lifted.Tag) extends Table[DbTag](tag, "tag") {
    def id = column[DbTagId]("tag_id", O.PrimaryKey)

    def name = column[String]("name")

    override def * = (id, name) <>(DbTag.tupled, DbTag.unapply)
  }

  object tags extends TableQuery(new Tags(_)) {

    def byBookId(bookId: Column[DbBookId]) = this innerJoin BookTagTable.bookTags where(_._2.bookId === bookId) map(_._1)

    val findByName = this.findBy(_.name)

    def cleanup = sqlu"""delete from tag where tag_id not in (select distinct tag_id from book_tag)"""
  }

}
