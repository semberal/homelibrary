package controllers

import play.api.mvc.{Action, Controller}
import models.Book

/**
 * @author Lukas Sembera <semberal@gmail.com>
 */
object AuthorController extends Controller {

  def authorPopoverContent(authorId: Long) = Action {
    implicit request =>
      Ok(views.html.tags.authorPopoverContent(Book.getBooks(authorId = Some(authorId))))
  }

}

