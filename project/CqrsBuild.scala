import sbt._
import sbt.Keys._

object CqrsBuild extends Build {

  lazy val cqrs = Project(
    id = "cqrs",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "cqrs",
      organization := "org.lucid",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      resolvers += "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/",
      libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.1",
      libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.4",
      scalacOptions ++= Seq("-unchecked", "-deprecation")
    )
  )
}
