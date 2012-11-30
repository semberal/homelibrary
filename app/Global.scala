import controllers.ControllerSupport
import play.api.mvc.{Results, RequestHeader}
import play.api.{Application, GlobalSettings}
import Results._


object Global extends GlobalSettings with ControllerSupport{
  override def onStart(app: Application) {
    super.onStart(app)
    play.Logger.info("Starting the application.")
  }

  override def onStop(app: Application) {
    super.onStop(app)
    play.Logger.info("Stopping the application.")
  }

  override def onRouteRequest(request: RequestHeader) = {
    play.Logger.info("Incoming request: %s" format request)
    super.onRouteRequest(request)
  }

  override def onHandlerNotFound(request: RequestHeader) = {
    implicit val request0 = request
    CustomNotFound
  }
}
