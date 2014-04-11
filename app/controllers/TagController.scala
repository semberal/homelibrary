package controllers

import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.db.slick.DB
import persist.BookTable.books

import scala.language.reflectiveCalls


object TagController extends Controller {
  def tagPopoverContent(tagId: Long) = Action {
    implicit request =>
      val b = DB.withTransaction {implicit session =>

        books.getAllBooks()
      }

      Ok(views.html.tags.tagPopoverContent(b)) // todo limit to tag

  }

}
