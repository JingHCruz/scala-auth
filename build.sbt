organization := "org.softnetwork"

name := "scala-auth"

version := "0.1.0"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.11.2", "2.10.4")

val akkaVersion = "2.3.3"

val sprayVersion = "1.3.1"

val specs2Version = "2.3.13"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= Seq(
  "io.spray"          %% "spray-routing" % sprayVersion  % "provided",
  "com.typesafe.akka" %% "akka-actor"    % akkaVersion   % "provided",
  "io.spray"          %% "spray-testkit" % sprayVersion  % "test",
  "org.specs2"        %% "specs2"        % specs2Version % "test"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/fupelaqu/scala-auth</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>http://opensource.org/licenses/mit-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:fupelaqu/scala-auth.git</url>
    <connection>scm:git:git@github.com:fupelaqu/scala-auth.git</connection>
  </scm>
  <developers>
    <developer>
      <id>smanciot</id>
      <name>Stéphane Manciot</name>
      <url>http://www.linkedin.com/in/smanciot</url>
    </developer>
  </developers>)
