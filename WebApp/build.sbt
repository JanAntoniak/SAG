import NativePackagerHelper._

name := "akka-sample-main-scala"

version := "2.4.11"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka"          %% "akka-actor"                % "2.5.12",
  "ch.qos.logback"              % "logback-classic"           % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging"             % "3.9.0",
  "org.mongodb.scala"          %% "mongo-scala-driver"        % "2.3.0",
  "org.jsoup"                   % "jsoup"                     % "1.11.2"
)

enablePlugins(JavaServerAppPackaging)

mainClass in (Compile, run) := Some("pl.sag.Main")

mappings in Universal ++= {
  // optional example illustrating how to copy additional directory
  directory("scripts") ++
  // copy configuration files to config directory
  contentOf("src/main/resources").toMap.mapValues("config/" + _)
}

// add 'config' directory first in the classpath of the start script,
// an alternative is to set the config file locations via CLI parameters
// when starting the application
scriptClasspath := Seq("../config/") ++ scriptClasspath.value

licenses := Seq(("CC0", url("http://creativecommons.org/publicdomain/zero/1.0")))
