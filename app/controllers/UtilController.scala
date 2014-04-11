package controllers

import play.api.mvc._
import play.api._
import persist.{TagTable, AuthorTable}
import play.api.db.slick.DB
import play.api.Play.current

object UtilController extends Controller with ControllerSupport {
  def listConfiguration = Authenticated {
    implicit request =>
      val keys = Play.current.configuration.keys.toList.sortWith(_ < _)
      Ok(keys.mkString("\n"))
  }

  def cleanup = Authenticated {
    implicit request =>
      val (x, y) = DB.withTransaction {
        implicit session =>
          (AuthorTable.authors.cleanup.first(), TagTable.tags.cleanup.first())
      }
      Ok((x + y).toString)
  }
}