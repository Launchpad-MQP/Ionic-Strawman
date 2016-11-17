import play.twirl.sbt.SbtTwirl

lazy val commonSettings = Seq(
  version := "1.0",
  organization := "org.combinators",
  
  libraryDependencies += "de.tu_dortmund.cs.ls14"%%"cls-scala"%"1.0",
  compileOrder := CompileOrder.JavaThenScala,

  scalaVersion := "2.11.8",

  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.typesafeRepo("releases")
  ),

  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-language:implicitConversions"
  )
)

lazy val root = (Project(id = "launchpadMQP", base = file(".")))
  .settings(commonSettings: _*)
  .enablePlugins(SbtTwirl)
  .settings(
    moduleName := "launchpadMQP",

    libraryDependencies ++= Seq(
      "de.tu_dortmund.cs.ls14" %% "cls-scala" % "1.0"
    ),

    sourceDirectories in (Compile, TwirlKeys.compileTemplates) := Seq(sourceDirectory.value / "main" / "templates")
  )
  

