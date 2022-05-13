ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "sparking",
    idePackagePrefix := Some("net.ingtra.sparking")
  )

resolvers ++= Seq(
  "Google Maven Central Asia" at "https://maven-central-asia.storage-download.googleapis.com",
  "Google Maven Central" at "https://maven-central.storage.googleapis.com"
)

val hadoopVersion = "3.3.1"
val sparkVersion = "3.2.1"
val icebergVersion = "0.13.1"
val circeVersion = "0.13.0"
val scalaUriVersion = "4.0.1"
val sttpVersion = "3.5.1"
val awsSdkVersion = "1.11.901"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
).map(_ % "provided")
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion
).map(_.exclude("org.spark-project.spark", "unused"))
libraryDependencies += "org.apache.iceberg" %% "iceberg-spark-runtime-3.2" % icebergVersion % "provided"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-yaml" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion
)
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % sttpVersion
libraryDependencies += "com.amazonaws" % "aws-java-sdk-bundle" % awsSdkVersion
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % hadoopVersion

Compile / run := Defaults.runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner).evaluated