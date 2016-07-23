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

// copy fastOptJS/fullOptJS  files to assets directory
val webAssetsDir = "scalajs_web/assets/"

lazy val scalaJsWebSettings = Seq(
  crossTarget in(Compile, fullOptJS) := file(webAssetsDir),
  crossTarget in(Compile, fastOptJS) := file(webAssetsDir),
  crossTarget in(Compile, packageScalaJSLauncher) := file(webAssetsDir),
    artifactPath in(Compile, fastOptJS) := ((crossTarget in(Compile, fastOptJS)).value /
    ((moduleName in fastOptJS).value + "-opt.js")),

  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature",
    "-language:postfixOps", "-language:implicitConversions",
    "-language:higherKinds", "-language:existentials")
)

lazy val scalajs_web = project.settings(commonSettings: _*)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.chandu0101.sri" %%% "web" % "0.5.0",
      "com.github.chandu0101.sri" %%% "scalacss" % "2016.5.0"
    )
  )
  .settings(scalaJsWebSettings: _*)

lazy val sputter = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "root"
  )
  .dependsOn(akka_http_sputter, scalajs_web)