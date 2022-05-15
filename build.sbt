ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

import sbt.Keys.{libraryDependencies}

lazy val root = (project in file("."))
  .settings(
    name := "tf_idf"
  )

val SparkVersion = "3.2.1"
val dependencyScope = "provided"
val HadoopVersion = "2.7.2"
val SparkCompatibleVersion = "3.2"
val ScalaCompatibleVersion = "2.13"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % SparkVersion,
  "org.apache.spark" %% "spark-sql" % SparkVersion,
  "org.apache.spark" %% "spark-mllib" % SparkVersion,
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % HadoopVersion % dependencyScope,
  "org.apache.hadoop" % "hadoop-common" % HadoopVersion % dependencyScope,
  "org.apache.hadoop" % "hadoop-hdfs" % HadoopVersion % dependencyScope,
  "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  "org.mockito" %% "mockito-scala" % "1.16.23" % Test
)
