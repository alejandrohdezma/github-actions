/*
 * Copyright 2021 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.github.actions

import scala.collection.immutable.ListMap

import com.alejandrohdezma.github.actions.yaml._

/**
 * A workflow run is made up of one or more jobs. Jobs run in parallel by default. To run jobs sequentially, you can
 * define dependencies on other jobs using the jobs.<job_id>.needs keyword.
 *
 * Each job runs in a fresh instance of the virtual environment specified by runs-on.
 *
 * You can run an unlimited number of jobs as long as you are within the workflow usage limits. For more information, see https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#usage-limits.
 *
 * @param name each job must have an id to associate with the job. The key job_id is a string and its value is a map of
 *   the job's configuration data. The job's name must be unique in the list of jobs. It must start with a letter or `_`
 *   and contain only alphanumeric characters, `-`, or `_`.
 * @param runsOn the type of machine to run the job on. The machine can be either a GitHub-hosted runner, or a
 *   self-hosted runner.
 * @param needs identifies any jobs that must complete successfully before this job will run. It can be a string or
 *   array of strings. If a job fails, all jobs that need it are skipped unless the jobs use a conditional statement
 *   that causes the job to continue.
 * @param environment the environment that the job references
 * @param env a map of environment variables that are available to all steps in the job
 * @param defaults a map of default settings that will apply to all steps in the job
 * @param `if` you can use the if conditional to prevent a job from running unless a condition is met. You can use any
 *   supported context and expression to create a conditional. Expressions in an if conditional do not require the
 *   \${{ }} syntax. For more information, see [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
 * @param outputs a map of outputs for a job. Job outputs are available to all downstream jobs that depend on this job
 * @param timeoutMinutes the maximum number of minutes to let a workflow run before GitHub automatically cancels it. The
 *   default is 360 minutes
 * @param continueOnError prevents a workflow run from failing when a job fails. Set to true to allow a workflow run to
 *   pass when this job fails
 * @param container a container to run any steps in a job that don't already specify a container. If you have steps that
 *   use both script and container actions, the container actions will run as sibling containers on the same network
 *   with the same volume mounts, otherwise; all steps will run directly on the host specified by runs-on unless a step
 *   refers to an action configured to run in a container
 * @param strategy a strategy creates a build matrix for your jobs. You can define different variations of an
 *   environment to run each job in
 * @param steps A job contains a sequence of tasks called steps. Steps can run commands, run setup tasks, or run an
 *   action in your repository, a public repository, or an action published in a Docker registry. Not all steps run
 *   actions, but all actions run as a step. Each step runs in its own process in the virtual environment and has access
 *   to the workspace and filesystem. Because steps run in their own process, changes to environment variables are not
 *   preserved between steps. GitHub provides built-in steps to set up and complete a job
 *
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#jobs]]
 */
final case class Job(
    id: Id,
    runsOn: Runner = Runner.HostedRunner.`ubuntu-latest`,
    name: Option[NotEmptyString] = None,
    needs: List[Id] = Nil,
    environment: Option[Environment] = None,
    env: Map[NotEmptyString, NotEmptyString] = ListMap(),
    defaults: Option[Defaults] = None,
    `if`: Option[Expression] = None,
    outputs: Map[NotEmptyString, NotEmptyString] = ListMap(),
    timeoutMinutes: Option[Int] = None,
    `continue-on-error`: Option[Either[Boolean, Expression]] = None,
    container: Option[Container] = None,
    services: List[Service] = Nil,
    strategy: Option[Strategy] = None,
    steps: NotEmptyList[Step]
) {

  def runsOn(runner: Runner): Job = copy(runsOn = runner)

  def runsOn(expression: Expression): Job = copy(runsOn = Runner.FromExpression(expression))

  def name(name: NotEmptyString): Job = copy(name = Some(name))

  def needs(jobs: Id*): Job = copy(needs = needs ++ jobs)

  def disableEnvironment(): Job = copy(environment = None)

  def environment(name: NotEmptyString): Job = environment(Environment(name))

  def environment(name: NotEmptyString, url: Url): Job = environment(Environment(name, Some(url)))

  def environment(environment: Environment): Job = copy(environment = Some(environment))

  def env(key: NotEmptyString, value: NotEmptyString): Job = copy(env = env + ((key, value)))

  def env(key: NotEmptyString, value: Expression): Job = env(key, value.show())

  def disableDefaults(): Job = copy(defaults = None)

  def defaults(shell: Shell, workingDirectory: NotEmptyString): Job =
    copy(defaults = Some(Defaults(Some(shell), Some(workingDirectory))))

  def defaults(shell: Shell): Job =
    copy(defaults = defaults.map(_.copy(shell = Some(shell))).orElse(Some(Defaults(shell = Some(shell)))))

  def defaults(workingDirectory: NotEmptyString): Job =
    copy(defaults =
      defaults
        .map(_.copy(workingDirectory = Some(workingDirectory)))
        .orElse(Some(Defaults(workingDirectory = Some(workingDirectory))))
    )

  def disableIf(): Job = copy(`if` = None)

  def runsIf(`if`: Expression): Job = copy(`if` = Some(`if`))

  def output(key: NotEmptyString, value: NotEmptyString): Job = copy(outputs = outputs + ((key, value)))

  def output(key: NotEmptyString, value: Expression): Job = output(key, value.show())

  def timeoutMinutes(timeoutMinutes: Int): Job = copy(timeoutMinutes = Some(timeoutMinutes))

  def continueOnError(): Job = copy(`continue-on-error` = Some(Left(true)))

  def doNotContinueOnError(): Job = copy(`continue-on-error` = Some(Left(false)))

  def continueOnErrorIf(expression: Expression): Job = copy(`continue-on-error` = Some(Right(expression)))

  def disableContainer(): Job = copy(container = None)

  def container(container: Container): Job = copy(container = Some(container))

  def container(image: NotEmptyString): Job = copy(container = Some(Container(image)))

  def service(name: NotEmptyString, container: Container): Job =
    copy(services = services :+ Service(name, container))

  def disableStrategy(): Job = copy(strategy = None)

  def strategy(strategy: Strategy): Job = copy(strategy = Some(strategy))

  def step(step: Step): Job = steps(step)

  def steps(first: Step, rest: Step*): Job = copy(steps = steps ::: NotEmptyList.of(first, rest: _*))

}

object Job {

  final case class Builder(
      id: Id,
      runsOn: Runner = Runner.HostedRunner.`ubuntu-latest`,
      name: Option[NotEmptyString] = None,
      needs: List[Id] = Nil,
      environment: Option[Environment] = None,
      env: Map[NotEmptyString, NotEmptyString] = ListMap(),
      defaults: Option[Defaults] = None,
      `if`: Option[Expression] = None,
      outputs: Map[NotEmptyString, NotEmptyString] = ListMap(),
      timeoutMinutes: Option[Int] = None,
      `continue-on-error`: Option[Either[Boolean, Expression]] = None,
      container: Option[Container] = None,
      services: List[Service] = Nil,
      strategy: Option[Strategy] = None
  ) {

    def runsOn(runner: Runner): Builder = copy(runsOn = runner)

    def runsOn(expression: Expression): Builder = copy(runsOn = Runner.FromExpression(expression))

    def name(name: NotEmptyString): Builder = copy(name = Some(name))

    def needs(jobs: Id*): Builder = copy(needs = needs ++ jobs)

    def disableEnvironment(): Builder = copy(environment = None)

    def environment(name: NotEmptyString): Builder = environment(Environment(name))

    def environment(name: NotEmptyString, url: Url): Builder = environment(Environment(name, Some(url)))

    def environment(environment: Environment): Builder = copy(environment = Some(environment))

    def env(key: NotEmptyString, value: NotEmptyString): Builder = copy(env = env + ((key, value)))

    def env(key: NotEmptyString, value: Expression): Builder = env(key, value.show())

    def disableDefaults(): Builder = copy(defaults = None)

    def defaults(shell: Shell, workingDirectory: NotEmptyString): Builder =
      copy(defaults = Some(Defaults(Some(shell), Some(workingDirectory))))

    def defaults(shell: Shell): Builder =
      copy(defaults = defaults.map(_.copy(shell = Some(shell))).orElse(Some(Defaults(shell = Some(shell)))))

    def defaults(workingDirectory: NotEmptyString): Builder =
      copy(defaults =
        defaults
          .map(_.copy(workingDirectory = Some(workingDirectory)))
          .orElse(Some(Defaults(workingDirectory = Some(workingDirectory))))
      )

    def disableIf(): Builder = copy(`if` = None)

    def runsIf(`if`: Expression): Builder = copy(`if` = Some(`if`))

    def output(key: NotEmptyString, value: NotEmptyString): Builder = copy(outputs = outputs + ((key, value)))

    def output(key: NotEmptyString, value: Expression): Builder = output(key, value.show())

    def output(key: NotEmptyString, outputFromStep: OutputFromStep): Builder =
      output(key, Expression.unsafe(s"steps.${outputFromStep.step}.outputs.${outputFromStep.name.getOrElse(key)}"))

    def timeoutMinutes(timeoutMinutes: Int): Builder = copy(timeoutMinutes = Some(timeoutMinutes))

    def continueOnError(): Builder = copy(`continue-on-error` = Some(Left(true)))

    def doNotContinueOnError(): Builder = copy(`continue-on-error` = Some(Left(false)))

    def continueOnErrorIf(expression: Expression): Builder = copy(`continue-on-error` = Some(Right(expression)))

    def disableContainer(): Builder = copy(container = None)

    def container(container: Container): Builder = copy(container = Some(container))

    def container(image: NotEmptyString): Builder = copy(container = Some(Container(image)))

    def service(name: NotEmptyString, container: Container): Builder =
      copy(services = services :+ Service(name, container))

    def disableStrategy(): Builder = copy(strategy = None)

    def strategy(strategy: Strategy): Builder = copy(strategy = Some(strategy))

    def step(step: Step): Job = steps(step)

    def steps(first: Step, rest: Step*): Job = Job(
      id = id,
      runsOn = runsOn,
      name = name,
      needs = needs,
      environment = environment,
      env = env,
      defaults = defaults,
      `if` = `if`,
      outputs = outputs,
      timeoutMinutes = timeoutMinutes,
      `continue-on-error` = `continue-on-error`,
      container = container,
      services = services,
      strategy = strategy,
      steps = NotEmptyList.of(first, rest: _*)
    )

  }

  implicit val JobEncoder: Encoder[Job] = job =>
    Yaml.obj(
      job.id.value := Yaml.obj(
        "name"              := job.name,
        "runs-on"           := job.runsOn,
        "needs"             := (if (job.needs.isEmpty) Yaml.Null else job.needs.asYaml),
        "if"                := job.`if`,
        "environment"       := job.environment,
        "env"               := (if (job.env.isEmpty) Yaml.Null else job.env.map(t => t._1.value -> t._2).asYaml),
        "defaults"          := job.defaults,
        "outputs"           := (if (job.outputs.isEmpty) Yaml.Null else job.outputs.map(t => t._1.value -> t._2).asYaml),
        "timeout-minutes"   := job.timeoutMinutes,
        "continue-on-error" := job.`continue-on-error`.map(_.fold(_.asYaml, _.asYaml)),
        "container"         := job.container,
        "services" := (if (job.services.isEmpty) Yaml.Null
                       else job.services.map(_.asYaml).reduce((a, b) => b.merge(a))),
        "strategy" := job.strategy,
        "steps"    := job.steps.map(_.fold(_.asYaml, _.asYaml))
      )
    )

}
