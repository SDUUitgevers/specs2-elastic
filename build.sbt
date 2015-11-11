organization := "nl.sdu"

name := "specs2-elastic"

description := "Tools that allow specs2 tests to use their own Elasticsearch instance"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "1.7.4",
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "1.7.4"
)

// Publishing
publishTo := {
  val artifactory = "http://srv1075bh.sdu.nl:8081/artifactory/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at artifactory + "utility-snapshots")
  else
    Some("releases" at artifactory + "utility-releases")
}

releaseSettings
