package com.alejandrohdezma.github.actions

import scala.collection.immutable.ListMap

import com.alejandrohdezma.github.actions.base.Expression
import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

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
 *   Expressions in an if conditional do not require the \${{ }} syntax. For more information, see
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
    action: NotEmptyString,
    `with`: Map[NotEmptyString, NotEmptyString] = ListMap(),
    id: Option[NotEmptyString] = None,
    `if`: Option[Expression] = None,
    name: Option[NotEmptyString] = None,
    env: Map[NotEmptyString, NotEmptyString] = ListMap(),
    `continue-on-error`: Option[Either[Boolean, Expression]] = None,
    timeoutMinutes: Option[Int] = None
) {

  def withInput(key: NotEmptyString, value: Expression): Uses = withInput(key, value.show())

  def withInput(key: NotEmptyString, value: NotEmptyString): Uses = copy(`with` = `with` + ((key, value)))

  def id(id: NotEmptyString): Uses =
    copy(id = Some(id))

  def runsIf(expression: Expression): Uses =
    copy(`if` = Some(expression))

  def name(name: NotEmptyString): Uses = copy(name = Some(name))

  def env(key: NotEmptyString, value: NotEmptyString): Uses = copy(env = env + ((key, value)))

  def env(key: NotEmptyString, value: Expression): Uses = env(key, value.show())

  def continueOnError(): Uses = copy(`continue-on-error` = Some(Left(true)))

  def doNotContinueOnError(): Uses = copy(`continue-on-error` = Some(Left(false)))

  def continueOnErrorIf(expression: Expression): Uses = copy(`continue-on-error` = Some(Right(expression)))

  def timeoutMinutes(timeoutMinutes: Int): Uses = copy(timeoutMinutes = Some(timeoutMinutes))

}

object Uses {

  implicit val UsesEncoder: Encoder[Uses] = step =>
    Yaml.obj(
      "name"              := step.name,
      "if"                := step.`if`,
      "id"                := step.id,
      "uses"              := step.action,
      "with"              := (if (step.`with`.isEmpty) Yaml.Null else step.`with`.map(t => t._1.value -> t._2).asYaml),
      "env"               := (if (step.env.isEmpty) Yaml.Null else step.env.map(t => t._1.value -> t._2).asYaml),
      "continue-on-error" := step.`continue-on-error`.map(_.fold(_.asYaml, _.asYaml)),
      "timeout-minutes"   := step.timeoutMinutes
    )

}
