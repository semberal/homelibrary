package controllers

import play.api.mvc._
import play.api.Play
import play.api.db.slick.DB
import play.api.Play.current
import persist.BookTable
import scala.language.reflectiveCalls


object IndexController extends Controller {

  def index = Action {
    implicit request =>
      val bookCount = Play.configuration.getInt("application.homepageBookCount").get // todo limit


      val books = DB.withTransaction {
        implicit session =>
          BookTable.books.getAllBooks
      }
      Ok(views.html.index(books))
  }

}