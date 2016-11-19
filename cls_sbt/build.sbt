lazy val root = (Project(id = "launchpadMQP", base = file(".")))
  .enablePlugins(SbtTwirl)
  .settings(
    version := "1.0",
    scalaVersion := "2.11.8",
    compileOrder := CompileOrder.JavaThenScala,

    sourceDirectories in (Compile, TwirlKeys.compileTemplates) := Seq(sourceDirectory.value / "main" / "templates")
  )
