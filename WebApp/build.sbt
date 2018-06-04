name := "akka-sample-main-scala"

scalaVersion := "2.12.4"

sbtVersion := "0.13.11"

val akkaVersion = "2.5.12"
val akkaHttpVersion = "10.1.1"
val breezeVersion = "0.13.2"

val dependencies: Seq[ModuleID] = Seq(
    "ch.qos.logback"              % "logback-classic"                   % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging"                     % "3.9.0",
    "org.jsoup"                   % "jsoup"                             % "1.11.2",
    "org.mongodb.scala"          %% "mongo-scala-driver"                % "2.3.0",
    "org.scalanlp"               %% "breeze"                            % breezeVersion,
    "org.scalanlp"               %% "breeze-natives"                    % breezeVersion,
    "org.scalanlp"               %% "breeze-viz"                        % breezeVersion,
    "com.typesafe.akka"          %% "akka-http"                         % akkaHttpVersion,
    "com.typesafe.akka"          %% "akka-http-spray-json"              % akkaHttpVersion,
    "com.typesafe.akka"          %% "akka-stream"                       % akkaVersion,
    "com.typesafe.akka"          %% "akka-actor"                        % akkaVersion,
    "com.typesafe.akka"          %% "akka-remote"                       % akkaVersion,
    "com.typesafe.akka"          %% "akka-cluster"                      % akkaVersion,
    "com.typesafe.akka"          %% "akka-cluster-metrics"              % akkaVersion,
    "com.typesafe.akka"          %% "akka-cluster-tools"                % akkaVersion
)

mainClass in (Compile, run) := Some("pl.sag.microservice.NodeCreator")

lazy val main: Project =
  (project in file("."))
    .settings(
      libraryDependencies ++= dependencies,
      name := "Main"
    )