ThisBuild / scalaVersion        := "2.12.13"
ThisBuild / organization        := "com.alejandrohdezma"
ThisBuild / libraryDependencies += "org.scalameta" %% "munit" % "0.7.23" % Test
ThisBuild / testOptions in Test += Tests.Argument(new TestFramework("munit.Framework"), "+l", "+n")
ThisBuild / libraryDependencies += compilerPlugin("org.scalamacros" %% "paradise" % "2.1.1" cross CrossVersion.full)
ThisBuild / libraryDependencies += compilerPlugin("org.typelevel" % "kind-projector" % "0.11.3" cross CrossVersion.full)

addCommandAlias("ci-test", "fix --check; mdoc; +test")
addCommandAlias("ci-docs", "github; headerCreateAll; mdoc")
addCommandAlias("ci-publish", "github; ci-release")

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))

lazy val `github-actions` = project
  .settings(
    Compile / doc / scalacOptions ++= Seq(
      "-doc-source-url",
      s"https://github.com/alejandrohdezma/github-actions/blob/${version.value}/github-actions/src/main€{FILE_PATH}.scala",
      "-doc-root-content",
      "scaladoc-root.txt"
    )
  )
  .settings(libraryDependencies += "com.softwaremill.quicklens" %% "quicklens" % "1.6.1" % Test)
  .settings(libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value)
