name := "RestAPI"

version := "0.1"

scalaVersion := "2.12.11"

sbtVersion := "1.2.8"

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor-typed" % "2.5.32",
  "com.typesafe.akka" %% "akka-stream" % "2.5.32",
  "com.typesafe.akka" %% "akka-http" % "10.2.4",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.4",

  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.flywaydb" % "flyway-core" % "4.2.0",
  "com.zaxxer" % "HikariCP" % "2.7.0",

  "org.postgresql" % "postgresql" % "42.1.4",
  "org.slf4j" % "slf4j-nop" % "1.6.4",

  "io.circe" %% "circe-core" % "0.12.3",
  "io.circe" %% "circe-generic" % "0.12.3",
  "io.circe" %% "circe-parser" % "0.12.3")

unmanagedSourceDirectories in Compile += baseDirectory.value / "main/resourses"
