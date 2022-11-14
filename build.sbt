ThisBuild / version := "0.2.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "sparking",
    idePackagePrefix := Some("net.ingtra.sparking")
  )

resolvers ++= Seq(
  "Google Maven Central Asia" at "https://maven-central-asia.storage-download.googleapis.com",
  "Google Maven Central" at "https://maven-central.storage.googleapis.com"
)

val sparkVersion = "3.3.0"
val icebergVersion = "0.14.1"
val circeVersion = "0.13.0"
val scalaUriVersion = "4.0.1"
val sttpVersion = "3.5.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-hadoop-cloud" % sparkVersion
).map(_ % "provided")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion
).map(_.exclude("org.spark-project.spark", "unused"))

libraryDependencies += "org.apache.iceberg" %% "iceberg-spark-runtime-3.3" % icebergVersion % "provided"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-yaml" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion
)

libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % sttpVersion

Compile / run := Defaults.runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner).evaluated