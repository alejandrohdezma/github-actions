package com.alejandrohdezma.github.actions

import scala.collection.immutable.ListMap

import com.alejandrohdezma.github.actions.base.Expression
import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

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
 *   Expressions in an if conditional do not require the \${{ }} syntax. For more information, see
 *   [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
 * @param name a name for your step to display on GitHub
 * @param env sets environment variables for steps to use in the virtual environment. You can also set environment
 *   variables for the entire workflow or a job
 * @param continueOnError prevents a job from failing when a step fails. Set to true to allow a job to pass when this
 *   step fails
 * @param timeoutMinutes the maximum number of minutes to run the step before killing the process
 */
final case class Run(
    command: NotEmptyString,
    workingDirectory: Option[NotEmptyString] = None,
    shell: Option[Shell] = None,
    id: Option[NotEmptyString] = None,
    `if`: Option[Expression] = None,
    name: Option[NotEmptyString] = None,
    env: Map[NotEmptyString, NotEmptyString] = ListMap(),
    `continue-on-error`: Option[Either[Boolean, Expression]] = None,
    timeoutMinutes: Option[Int] = None
) {

  def workingDirectory(workingDirectory: NotEmptyString): Run =
    copy(workingDirectory = Some(workingDirectory))

  def shell(shell: Shell): Run =
    copy(shell = Some(shell))

  def id(id: NotEmptyString): Run =
    copy(id = Some(id))

  def runsIf(expression: Expression): Run =
    copy(`if` = Some(expression))

  def name(name: NotEmptyString): Run = copy(name = Some(name))

  def env(key: NotEmptyString, value: NotEmptyString): Run = copy(env = env + ((key, value)))

  def env(key: NotEmptyString, value: Expression): Run = env(key, value.show())

  def continueOnError(): Run = copy(`continue-on-error` = Some(Left(true)))

  def doNotContinueOnError(): Run = copy(`continue-on-error` = Some(Left(false)))

  def continueOnErrorIf(expression: Expression): Run = copy(`continue-on-error` = Some(Right(expression)))

  def timeoutMinutes(timeoutMinutes: Int): Run = copy(timeoutMinutes = Some(timeoutMinutes))

}

object Run {

  implicit val RunEncoder: Encoder[Run] = step =>
    Yaml.obj(
      "name"              := step.name,
      "if"                := step.`if`,
      "id"                := step.id,
      "run"               := step.command,
      "working-directory" := step.workingDirectory,
      "shell"             := step.shell,
      "env"               := (if (step.env.isEmpty) Yaml.Null else step.env.map(t => t._1.value -> t._2).asYaml),
      "continue-on-error" := step.`continue-on-error`.map(_.fold(_.asYaml, _.asYaml)),
      "timeout-minutes"   := step.timeoutMinutes
    )

}
