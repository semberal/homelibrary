package controllers

import play.api.mvc._

object IndexController extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

}