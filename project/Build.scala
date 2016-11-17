import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "homelibrary"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
	  "com.h2database" % "h2" % "1.3.168"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
