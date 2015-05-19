name := "slack-client"

organization := "com.ovoenergy"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq (
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "io.spray" %% "spray-routing" % "1.3.2",
  "io.spray" %% "spray-client" % "1.3.2",
  "io.spray" %% "spray-http" % "1.3.2",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.json4s" %% "json4s-ext" % "3.2.11",
  "org.glassfish.tyrus.bundles" % "tyrus-standalone-client" % "1.10",
  "javax.websocket" % "javax.websocket-api" % "1.1",
  "org.scalaz" %% "scalaz-core" % "7.0.6",
  "com.netaporter" %% "scala-uri" % "0.4.6",
  "io.reactivex" %% "rxscala" % "0.24.1",
  "org.specs2" %% "specs2-core" % "3.6" % "test"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

scalacOptions in Test ++= Seq("-Yrangepos")

Revolver.settings