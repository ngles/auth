name := "auth"

version := "0.1"

scalaVersion := "2.13.2"

organization := "org.bheaver.ngl4"


lazy val service = (project in file("service"))
  .settings(
    scalaVersion := "2.13.2",
    libraryDependencies += "org.bheaver.ngl4" % "businessmodel_2.13" % "0.1",
    libraryDependencies += "org.bheaver.ngl4" %% "base" % "0.1",
    libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.12",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.26",
    libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12",
    libraryDependencies += "com.google.inject" % "guice" % "4.2.3",
    libraryDependencies += "com.pauldijou" %% "jwt-core" % "4.2.0",

    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"

  )

lazy val root = (project in file(".")).aggregate(service)

