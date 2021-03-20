package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Run
import com.alejandrohdezma.github.actions.Uses
import com.alejandrohdezma.github.actions.base.NotEmptyString

trait Steps {

  /**
   * A job contains a sequence of tasks called steps. Steps can run commands, run setup tasks, or run an action in your
   * repository, a public repository, or an action published in a Docker registry.
   *
   * You must take the next into account when creating a new step:
   *
   * - Not all steps run actions, but all actions run as a step.
   * - Each step runs in its own process in the virtual environment and has access to the workspace and filesystem.
   * - Because steps run in their own process, changes to environment variables are not preserved between steps.
   * - GitHub provides built-in steps to set up and complete a job.
   *
   * @param id a unique identifier for the step. You can use the id to reference the step in contexts. For more
   *   information, see [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
   * @param if you can use the if conditional to prevent a step from running unless a condition is met. You can use any
   *   supported context and expression to create a conditional.
   *   Expressions in an if conditional do not require the \${{ }} syntax. For more information, see
   *   [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
   * @param name a name for your step to display on GitHub
   * @param env sets environment variables for steps to use in the virtual environment. You can also set environment
   *   variables for the entire workflow or a job
   * @param continueOnError prevents a job from failing when a step fails. Set to true to allow a job to pass when this
   *   step fails
   * @param timeoutMinutes the maximum number of minutes to run the step before killing the process
   */
  type Step = Either[Uses, Run]

  def run(command: NotEmptyString): Run                = Run(command.stripMargin)
  implicit def Run2Either(run: Run): Either[Uses, Run] = Right(run) /* scalafix:ok DisableSyntax.implicitConversion */

  def uses(action: NotEmptyString): Uses                 = Uses(action)
  implicit def Uses2Either(run: Uses): Either[Uses, Run] = Left(run) /* scalafix:ok DisableSyntax.implicitConversion */

}
