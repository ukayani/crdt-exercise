import scala.sys.process.Process

name := "crdt-exercise"

version := "0.1"

scalaVersion := "2.12.6"
scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates" // Warn if a private member is unused.
)

libraryDependencies ++= Seq("net.debasishg" %% "redisclient" % "3.6", "org.scalatest" %% "scalatest" % "3.0.5" % "test")

scalafmtOnCompile in ThisBuild := true // all projects
scalafmtOnCompile := true // current project
scalafmtOnCompile in Compile := true // current project, specific configuration

parallelExecution in Test := false

testOptions in Test += Tests.Setup { () =>
  Process(s"docker-compose up -d").!
}
testOptions in Test += Tests.Cleanup { () =>
  Process(s"docker-compose down").!
}
