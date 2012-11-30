package controllers

import play.api.mvc._
import Results._


/**
 * @author Lukas Sembera <semberal@gmail.com>
 */
trait ControllerSupport {

  def CustomNotFound(implicit requestHeader: RequestHeader) = {
    NotFound(views.html.notFound())
  }

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
