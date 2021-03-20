ThisBuild / scalaVersion        := "2.12.13"
ThisBuild / organization        := "com.alejandrohdezma"
ThisBuild / libraryDependencies += "org.scalameta" %% "munit" % "0.7.22" % Test
ThisBuild / testFrameworks      += new TestFramework("munit.Framework")
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
  .settings(libraryDependencies += "com.beachape" %% "enumeratum" % "1.6.1")
  .settings(libraryDependencies += "com.beachape" %% "enumeratum-cats" % "1.6.1")
  .settings(libraryDependencies += "com.beachape" %% "enumeratum-circe" % "1.6.1")
  .settings(libraryDependencies += "org.typelevel" %% "cats-core" % "2.4.2")
  .settings(libraryDependencies += "eu.timepit" %% "refined" % "0.9.21")
  .settings(libraryDependencies += "eu.timepit" %% "refined-cats" % "0.9.21")
  .settings(libraryDependencies += "io.circe" %% "circe-core" % "0.13.0")
  .settings(libraryDependencies += "io.circe" %% "circe-refined" % "0.13.0")
  .settings(libraryDependencies += "io.circe" %% "circe-yaml" % "0.13.1")
