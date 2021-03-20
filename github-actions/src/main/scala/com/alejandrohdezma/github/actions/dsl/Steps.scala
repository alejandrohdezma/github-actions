package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Run
import com.alejandrohdezma.github.actions.Uses
import com.alejandrohdezma.github.actions.base.NonEmptyString

/** Contains aliases and smart-constructors for creatings job steps. */
trait Steps {

  /** A job contains a sequence of tasks called steps. Steps can run commands, run setup tasks, or run an action in your
    * repository, a public repository, or an action published in a Docker registry.
    *
    * You must take the next into account when creating a new step:
    *
    *   - Not all steps run actions, but all actions run as a step.
    *   - Each step runs in its own process in the virtual environment and has access to the workspace and filesystem.
    *   - Because steps run in their own process, changes to environment variables are not preserved between steps.
    *   - GitHub provides built-in steps to set up and complete a job.
    */
  type Step = Either[Uses, Run]

  /** Creates a [[Run run]] step that executes the provided command. The generated step
    * can then be populated using the available DSL.
    *
    * @example {{{
    * run("sbt test")
    *  .name("Run the project's tests")
    *  .runsIf(is[pullRequest])
    *  .env("GITHUB_TOKEN", secrets.ADMIN_GITHUB_TOKEN)
    * }}}
    */
  def run(command: NonEmptyString): Run = Run(command.stripMargin)

  /** Creates a [[Uses uses]] step that executes a certain action. The generated step
    * can then be populated using the available DSL.
    *
    * @example {{{
    * uses("scala-steward-org/scala-steward-action@v2")
    *   .name("Launch Scala Steward")
    *   .withInput("github-token", secrets.REPO_GITHUB_TOKEN)
    *   .withInput("author-email", "alejandrohdezma@users.noreply.github.com")
    *   .withInput("author-name", "alejandrohdezma[bot]")
    * }}}
    */
  def uses(action: NonEmptyString): Uses = Uses(action)

  /** Allows using [[run]] directly when creating an step without needing to call `Right`. */
  implicit def Run2Either(run: Run): Step = Right(run) /* scalafix:ok DisableSyntax.implicitConversion */

  /** Allows using [[uses]] directly when creating an step without needing to call `Left`. */
  implicit def Uses2Either(run: Uses): Step = Left(run) /* scalafix:ok DisableSyntax.implicitConversion */

}
