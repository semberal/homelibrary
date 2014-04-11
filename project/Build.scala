import sbt._
import sbt.Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "homelibrary"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      jdbc, 
      "com.h2database" % "h2" % "1.3.168",
      "com.typesafe.play" %% "play-slick" % "0.6.0.1",
      "com.typesafe.slick" %% "slick" % "2.0.1"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
    )

}
