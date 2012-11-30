package controllers

import play.api.mvc._
import models.Book

object IndexController extends Controller {

  def index = Action { implicit request =>
    val books = Book.getBooks()

    Ok(views.html.index(books.take(4)))
  }

}