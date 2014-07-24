organization := "org.softnetwork"

name := "scala-auth"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

val akkaVersion = "2.2.3"

val sprayVersion = "1.3.1"

val specs2Version = "2.2.3"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= Seq(
  "io.spray"           % "spray-routing" % sprayVersion  % "provided",
  "com.typesafe.akka" %% "akka-actor"    % akkaVersion   % "provided",
  "io.spray"           % "spray-testkit" % sprayVersion,
  "org.specs2"        %% "specs2"        % specs2Version % "test"
)
