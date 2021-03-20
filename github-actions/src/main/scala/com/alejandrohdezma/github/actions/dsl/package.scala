package com.alejandrohdezma.github.actions

/** Provides a complete DSL for creating GitHub Actions.
  *
  * ==Overview==
  * This is the main and entrypoint for the library. By importing this package contents you'll have
  * access to the whole DSL.
  *
  * ==Usage==
  * {{{
  * import com.alejandrohdezma.github.actions.dsl._
  * }}}
  *
  * ==DSL==
  * The main entrypoints that you are probably looking for are `workflowFile` and `workflow` (most of the rest
  * of the DSL will hang from this entrypoint):
  *
  * {{{
  * workflows += workflowFile("build") {
  *   workflow
  *   .name("Scala CI")
  *   .on(push.branches(matching("main")))
  *   .on(pullRequest.branches(matching("main")))
  *   .job("build") {
  *     _.runsOn(`ubuntu-latest`)
  *       .steps(
  *         uses("actions/checkout@v2"),
  *         uses("actions/setup-java@v2")
  *           .name("Set up JDK 11")
  *           .withInput("java-version", "11")
  *           .withInput("distribution", "adopt"),
  *         run("sbt test")
  *           .name("Run tests")
  *       )
  *   }
  * }
  * }}}
  *
  * This package also provides other aspects of the DSL like:
  *
  *   - Access to the different [[dsl.Contexts contexts]] to be used on expressions.
  *   - A suite of common [[dsl.Expressions expressions]] that can be used to eliminate boilerplate.
  *   - Entrypoints for creating [[dsl.Steps steps]], [[dsl.Runners runners]], [[dsl.Shells shells]] or
  *     [[dsl.Events events]].
  *
  * @note Importing this object won't be needed if using the DSL from SBT, only from `.scala` files.
  * @see [[https://docs.github.com/en/actions To know more about GitHub Actions...]]
  */
package object dsl extends Dsl
