import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

name := "sputter"

lazy val commonSettings = Seq(
  organization := "com.github.boosh",

  version := "0.1.0-SNAPSHOT",

  scalaVersion := "2.11.8",

  scalacOptions ++= Seq("-deprecation", "-feature"),

  scalacOptions in Test ++= Seq("-Yrangepos"),

  libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.11" % "3.0.0-RC4" % "test"
  )
)

lazy val sputterRoot = (project in file("."))
  .settings(commonSettings: _*)
  .settings(Seq(
    publish := {},
    publishLocal := {}
  ))
  .aggregate(sputterJvm, sputterJs)

lazy val sputter = crossProject.in(file("."))
  .settings(
    name := "sputter"
  )
  .jvmSettings(commonSettings: _*)
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.0",
//      "com.typesafe.akka" %% "akka-actor" % "2.4.8",
      "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.4.8",
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.8",
      "com.typesafe.akka" %% "akka-slf4j" % "2.4.8",

      // logging
      "org.slf4j" % "slf4j-api" % "1.7.21",
      "org.slf4j" % "slf4j-log4j12" % "1.7.21",

      // image processing
      "com.sksamuel.scrimage" % "scrimage-core_2.11" % "2.1.6",

      // security
      "com.nimbusds" % "nimbus-jose-jwt" % "4.22",

      // third party SDKs
      "com.amazonaws" % "aws-java-sdk-s3" % "1.11.19",

      // type-safe REST calls
      "com.lihaoyi" %% "autowire" % "0.2.5",
      "com.lihaoyi" %% "upickle" % "0.4.1",

      // utils
      "commons-io" % "commons-io" % "2.4",
      "org.jsoup" % "jsoup" % "1.9.2",
      "org.scalaz" %% "scalaz-core" % "7.2.3",

      // testing
      "com.typesafe.akka" %% "akka-http-testkit" % "2.4.8" % "test"
    )
  )
  .jsSettings(commonSettings: _*)
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.0",
      "com.lihaoyi" %%% "upickle" % "0.4.1",
      "com.lihaoyi" %%% "autowire" % "0.2.5"
    )
  )

lazy val sputterJvm = sputter.jvm
lazy val sputterJs = sputter.js
