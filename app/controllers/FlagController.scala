package controllers

import play.api.mvc.{Action, SimpleResult, ChunkedResult, Controller}
import play.api.libs.iteratee.Enumerator

/**
 * @author Lukas Sembera <semberal@gmail.com>
 */
object FlagController extends Controller {

  private val flagDirectory = "/public/images/flags/"

  def getFlag(code: String) = Action {

    val flagStream = mkStream(flagDirectory + code + ".png")

    if (flagStream != null)
      Ok.stream(Enumerator.fromStream(flagStream)).as("image/png")
    else
      Ok.stream(Enumerator.fromStream(mkStream(defaultFlag))).as("image/png")
  }


  def defaultFlag = flagDirectory + "default.png"

  def mkStream(filePath: String) = getClass.getResourceAsStream(filePath)


}
