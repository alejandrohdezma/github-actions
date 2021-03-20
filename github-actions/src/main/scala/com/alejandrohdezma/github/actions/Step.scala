package com.alejandrohdezma.github.actions

import cats.data.NonEmptyMap

import io.circe.Encoder
import io.circe.Json
import io.circe.syntax._

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
 *   Expressions in an if conditional do not require the ${{ }} syntax. For more information, see
 *   [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
 * @param name a name for your step to display on GitHub
 * @param env sets environment variables for steps to use in the virtual environment. You can also set environment
 *   variables for the entire workflow or a job
 * @param continueOnError prevents a job from failing when a step fails. Set to true to allow a job to pass when this
 *   step fails
 * @param timeoutMinutes the maximum number of minutes to run the step before killing the process
 */
sealed abstract class Step(
    val id: Option[String],
    val `if`: Option[Expression],
    val name: Option[String],
    val env: Option[NonEmptyMap[String, String]],
    val continueOnError: Option[Either[Boolean, Expression]],
    val timeoutMinutes: Option[Int]
)

object Step {

  implicit val StepEncoder: Encoder[Step] = {
    case step: Run =>
      Json.obj(
        "run"               := step.command,
        "working-directory" := step.workingDirectory,
        "shell"             := step.shell,
        "name"              := step.name,
        "id"                := step.id,
        "if"                := step.`if`,
        "env"               := step.env,
        "continue-on-error" := step.continueOnError.map(_.fold(_.asJson, _.asJson)),
        "timeout-minutes"   := step.timeoutMinutes
      )
    case step: Uses =>
      Json.obj(
        "uses"              := step.action,
        "with"              := step.`with`,
        "name"              := step.name,
        "id"                := step.id,
        "if"                := step.`if`,
        "env"               := step.env,
        "continue-on-error" := step.continueOnError.map(_.fold(_.asJson, _.asJson)),
        "timeout-minutes"   := step.timeoutMinutes
      )
  }

  /**
   * Selects an action to run as part of a step in your job. An action is a reusable unit of code. You can use an action
   * defined in the same repository as the workflow, a public repository, or in a published Docker container image
   * ([[https://hub.docker.com/]]).
   *
   * We strongly recommend that you include the version of the action you are using by specifying a Git ref, SHA, or
   * Docker tag number. If you don't specify a version, it could break your workflows or cause unexpected behavior when
   * the action owner publishes an update.
   *
   *   - Using the commit SHA of a released action version is the safest for stability and security.
   *   - Using the specific major action version allows you to receive critical fixes and security patches while still
   *     maintaining compatibility. It also assures that your workflow should still work.
   *   - Using the master branch of an action may be convenient, but if someone releases a new major version with a
   *     breaking change, your workflow could break.
   *
   * Some actions require inputs that you must set using the with keyword. Review the action's README file to determine
   * the inputs required.
   *
   * Actions are either JavaScript files or Docker containers. If the action you're using is a Docker container you must
   * run the job in a Linux virtual environment.
   *
   * @param action the action to run
   * @param with a map of the input parameters defined by the action. Each input parameter is a key/value pair. Input
   *   parameters are set as environment variables. The variable is prefixed with INPUT_ and converted to upper case
   * @param id a unique identifier for the step. You can use the id to reference the step in contexts. For more
   *   information, see [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
   * @param if you can use the if conditional to prevent a step from running unless a condition is met. You can use any
   *   supported context and expression to create a conditional.
   *   Expressions in an if conditional do not require the ${{ }} syntax. For more information, see
   *   [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
   * @param name a name for your step to display on GitHub
   * @param env sets environment variables for steps to use in the virtual environment. You can also set environment
   *   variables for the entire workflow or a job
   * @param continueOnError prevents a job from failing when a step fails. Set to true to allow a job to pass when this
   *   step fails
   * @param timeoutMinutes the maximum number of minutes to run the step before killing the process
   *
   * @see [[https://help.github.com/en/articles/virtual-environments-for-github-actions]]
   */
  final case class Uses(
      val action: String,
      val `with`: Option[NonEmptyMap[String, String]] = None,
      override val id: Option[String] = None,
      override val `if`: Option[Expression] = None,
      override val name: Option[String] = None,
      override val env: Option[NonEmptyMap[String, String]] = None,
      override val continueOnError: Option[Either[Boolean, Expression]] = None,
      override val timeoutMinutes: Option[Int] = None
  ) extends Step(id, `if`, name, env, continueOnError, timeoutMinutes)

  /**
   * Step that runs a command in the virtual environment. Commands run using non-login shells by default. You can choose
   * a different shell and customize the shell used to run commands.
   *
   * Each run keyword represents a new process and shell in the virtual environment. When you provide multi-line
   * commands, each line runs in the same shell.
   *
   * @param command runs command-line programs using the operating system's shell. If you do not provide a name, the
   *   step name will default to the text specified in the run command.
   * @param shell the default shell settings in the runner's operating system. You can use built-in shell keywords, or
   *   you can define a custom set of shell options.
   * @param workingDirectory the working directory of where to run the command
   * @param id a unique identifier for the step. You can use the id to reference the step in contexts. For more
   *   information, see [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
   * @param if you can use the if conditional to prevent a step from running unless a condition is met. You can use any
   *   supported context and expression to create a conditional.
   *   Expressions in an if conditional do not require the ${{ }} syntax. For more information, see
   *   [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
   * @param name a name for your step to display on GitHub
   * @param env sets environment variables for steps to use in the virtual environment. You can also set environment
   *   variables for the entire workflow or a job
   * @param continueOnError prevents a job from failing when a step fails. Set to true to allow a job to pass when this
   *   step fails
   * @param timeoutMinutes the maximum number of minutes to run the step before killing the process
   */
  final case class Run(
      val command: String,
      val workingDirectory: Option[String] = None,
      val shell: Option[Shell] = None,
      override val id: Option[String] = None,
      override val `if`: Option[Expression] = None,
      override val name: Option[String] = None,
      override val env: Option[NonEmptyMap[String, String]] = None,
      override val continueOnError: Option[Either[Boolean, Expression]] = None,
      override val timeoutMinutes: Option[Int] = None
  ) extends Step(id, `if`, name, env, continueOnError, timeoutMinutes)

}
