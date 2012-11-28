package controllers

import play.api.mvc.{Action, SimpleResult, ChunkedResult, Controller}
import play.api.libs.iteratee.Enumerator

object FlagController extends Controller {

  private val flagDirectory = "/public/images/flags/"

  private val defaultFlag = flagDirectory + "default.png"

  private def mkStream(filePath: String) = getClass.getResourceAsStream(filePath)

  def getFlag(code: String) = Action {

    val flagStream = mkStream(flagDirectory + code + ".png")

    if (flagStream != null)
      Ok.stream(Enumerator.fromStream(flagStream)).as("image/png")
    else
      Ok.stream(Enumerator.fromStream(mkStream(defaultFlag))).as("image/png")
  }





}
