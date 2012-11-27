import play.api.mvc.RequestHeader
import play.api.{Application, GlobalSettings}


object Global extends GlobalSettings{
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
  
  
}
