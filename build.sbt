name := "sputter"

lazy val commonSettings = Seq(
  organization := "com.github.boosh",

  version := "0.1.0",

  scalaVersion := "2.11.8",

  scalacOptions ++= Seq("-deprecation", "-feature"),

  scalacOptions in Test ++= Seq("-Yrangepos"),

  libraryDependencies ++= Seq(
  )
)

lazy val akka_http_sputter = project.settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.0",
      "com.typesafe.akka" %% "akka-actor" % "2.4.8",
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

      // utils
      "commons-io" % "commons-io" % "2.4",
      "org.jsoup" % "jsoup" % "1.9.2",
      "org.scalaz" %% "scalaz-core" % "7.2.3",

      // testing
      "org.scalatest" %% "scalatest" % "2.2.6" % "test",
      "com.typesafe.akka" %% "akka-http-testkit" % "2.4.8" % "test"
    )
  )

lazy val scalajs_sputter = project.settings(commonSettings: _*)

lazy val sputter = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "root"
  )
  .dependsOn(akka_http_sputter, scalajs_sputter)