package controllers

import play.api.mvc.{ Results, Request }
import play.api.mvc.Action
import play.api.mvc.Result
import play.api.mvc.AnyContent
import Results._

trait ControllerSupport {


  def refererRedirect()(implicit request: Request[_]) = {
    Redirect(request.headers.get("Referer").getOrElse("/"))
  }

  def AuthAction(f: Request[AnyContent] => Result): Action[AnyContent] = {
    Action { request =>
      if (request.session.get("authenticated").isDefined)
        f(request)
      else
        Redirect(routes.IndexController.index).flashing("level" ->"error", "msg" -> "You are not authorized, please log in")
    }
  }
}
