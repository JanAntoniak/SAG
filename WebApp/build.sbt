name := "akka-sample-main-scala"

scalaVersion := "2.12.4"

sbtVersion := "0.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka"          %% "akka-actor"                % "2.5.12",
  "ch.qos.logback"              % "logback-classic"           % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging"             % "3.9.0",
  "org.mongodb.scala"          %% "mongo-scala-driver"        % "2.3.0",
  "org.jsoup"                   % "jsoup"                     % "1.11.2",
  "org.scalanlp"               %% "breeze"                    % "0.13.2",
  "org.scalanlp"               %% "breeze-natives"            % "0.13.2",
  "org.scalanlp"               %% "breeze-viz"                % "0.13.2"
)

mainClass in (Compile, run) := Some("pl.sag.Main")

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"