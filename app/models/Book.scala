package models

import java.util.Date
import play.api.db.DB
import anorm._
import play.api.Play.current
import anorm.SqlParser._
import Defaults.optionToPk

case class Book(id: Pk[Long],
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
                tags: List[Tag])

object Book {

  private val bookRowParser =
    get[Pk[Long]]("book_id") ~
      get[Option[String]]("isbn10") ~
      get[Option[String]]("isbn13") ~
      get[String]("title") ~
      Author.authorParser ~
      get[Option[String]]("description") ~
      get[Option[String]]("publisher") ~
      get[Option[Date]]("date_published") ~
      get[Option[String]]("lang") ~
      get[Option[Int]]("page_count") ~
      get[Option[String]]("notes") ~
      get[Option[String]]("cover_picture_url") ~
      Tag.tagParser map {
      // non-existing TupleFlattener for so many elements, cannot use .map(flatten)
      case id ~ isbn10 ~ isbn13 ~ title ~ author ~ description ~ publisher ~ datePublished ~ lang ~ pageCount ~ notes ~ coverPictureUrl ~ tags =>
        (id, isbn10, isbn13, title, author, description, publisher, datePublished, lang, pageCount, notes, coverPictureUrl, tags)
    }

  private val bookQuery =
    """select book.book_id as book_id, isbn10, isbn13, title, description, publisher, date_published, lang, page_count, notes, cover_picture_url, author.author_id, author.name, tag.tag_id, tag.name from book
             left join book_author on book.book_id = book_author.book_id inner join author on book_author.author_id = author.author_id
             left join book_tag on book.book_id = book_tag.book_id left join tag on book_tag.tag_id = tag.tag_id """

  private val bookQueryWithId = bookQuery + """where book.book_id = {book_id}"""

  def save(book: Book): Book = {
    DB.withConnection {
      implicit c =>

        val bookId: Option[Long] = book.id match {
          case Id(value) =>
            /* update values */
            val updateSql = """update book set isbn10={isbn10}, isbn13={isbn13}, title={title}, description={description}, publisher={publisher}, date_published={datePublished}, lang={lang}, page_count = {pageCount}, notes={notes} where book_id = {bookId}"""
            SQL(updateSql).on("isbn10" -> book.isbn10, "isbn13" -> book.isbn13, "title" -> book.title,
              "description" -> book.description, "publisher" -> book.publisher, "datePublished" -> book.datePublished,
              "lang" -> book.language, "pageCount" -> book.pageCount, "notes" -> book.notes, "bookId" -> value).executeUpdate()

            /* delete join tables for the book (new entries will be stored later) */
            SQL("""delete from book_tag where book_id = {bookId}""").on("bookId" -> value).executeUpdate()
            SQL("""delete from book_author where book_id = {bookId}""").on("bookId" -> value).executeUpdate()

            Some(value)

          case NotAssigned =>
            val sql = """insert into book values (DEFAULT, {isbn10}, {isbn13}, {title}, {description}, {publisher}, {datePublished}, {lang}, {pageCount}, {notes}, {coverPictureUrl})"""

            SQL(sql).on("isbn10" -> book.isbn10, "isbn13" -> book.isbn13, "title" -> book.title,
              "description" -> book.description, "publisher" -> book.publisher, "datePublished" -> book.datePublished,
              "lang" -> book.language, "pageCount" -> book.pageCount, "notes" -> book.notes,
              "coverPictureUrl" -> book.coverPictureUrl).executeInsert()
        }

        // M:N authors
        book.authors.distinct.map {
          author => Author.getAuthor(author.name).getOrElse(Author.saveAuthor(Author(NotAssigned, author.name))) //todo simplify???
        } foreach (author => {
          SQL( """insert into book_author values ({bookId}, {authorId})""").on("bookId" -> bookId, "authorId" -> author.id).executeInsert()
        })

        // M:N tags
        book.tags.distinct.map {
          tag => Tag.getTag(tag.name).getOrElse(Tag.saveTag(Tag(NotAssigned, tag.name)))
        }.foreach(tag => {
          SQL( """insert into book_tag values ({bookId}, {tagId})""").on("bookId" -> bookId, "tagId" -> tag.id).executeInsert()
        })



        book.copy(id = bookId)
    }

  }

  def getBook(bookId: Long): Option[Book] =
    DB.withConnection {
      implicit c =>
        val sql = bookQueryWithId
        val result = SQL(sql).on("book_id" -> bookId).as(bookRowParser *)
        result.map(_.copy(_5 = result.map(_._5).distinct, _13 = result.map(_._13).flatten.distinct)).headOption.map(tuple => (Book.apply _).tupled(tuple))
    }

  def getBooks(authorId: Option[Long] = None, tagId: Option[Long] = None): List[Book] = {
    DB.withConnection {
      implicit c =>
        val list = SQL(bookQuery).as(bookRowParser *).groupBy(_._1).values.map {
          tupleList => tupleList.map(_.copy(_5 = tupleList.map(_._5).distinct, _13 = tupleList.map(_._13).flatten.distinct)).headOption
        }.flatten.map(tuple => (Book.apply _).tupled(tuple)).toList

        val authorFilteredList = authorId match {
          case Some(id) => list.filter(_.authors.exists(_.id.get == id))
          case None => list
        }
        tagId match {
          case Some(id) => authorFilteredList.filter(_.tags.exists(_.id.get == id))
          case None => authorFilteredList
        }
    }
  }

  def deleteBook(id: Long) = {
    DB.withConnection {
      implicit c =>
        SQL("delete from book where book_id = {id}").on("id" -> id).executeUpdate()
    }
  }
}

case class SearchResult(query: String, totalResults: Int, page: Int, results: List[Book])