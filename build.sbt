organization := "nl.sdu.elastic"

name := "specs2-elastic"

description := "Tools that allow specs2 tests to use their own Elasticsearch instance"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "2.3.0" % Provided,
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "2.3.0" % Provided
)

// Publishing
publishTo := {
  val artifactory = "http://srv1075bh.sdu.nl:8081/artifactory/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at artifactory + "cwc-snapshots")
  else
    Some("releases" at artifactory + "cwc-releases")
}

releaseSettings
