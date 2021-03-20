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

import cats.data.NonEmptyList
import cats.data.NonEmptyMap

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import io.circe.Encoder
import io.circe.Json
import io.circe.refined._
import io.circe.syntax._

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
 * @param name the name of the job displayed on GitHub
 * @param needs identifies any jobs that must complete successfully before this job will run. It can be a string or
 *   array of strings. If a job fails, all jobs that need it are skipped unless the jobs use a conditional statement
 *   that causes the job to continue.
 * @param environment the environment that the job references
 * @param env a map of environment variables that are available to all steps in the job
 * @param defaults a map of default settings that will apply to all steps in the job
 * @param `if` you can use the if conditional to prevent a job from running unless a condition is met. You can use any
 *   supported context and expression to create a conditional. Expressions in an if conditional do not require the
 *   ${{ }} syntax. For more information, see [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
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
    id: String Refined MatchesRegex[W.`"^[_a-zA-Z][a-zA-Z0-9_-]*$"`.T],
    runsOn: RunsOn = RunsOn.HostedRunner.`ubuntu-latest`,
    name: Option[String] = None,
    needs: Option[NonEmptyList[String Refined MatchesRegex[W.`"^[_a-zA-Z][a-zA-Z0-9_-]*$"`.T]]] = None,
    environment: Option[Environment] = None,
    env: Option[NonEmptyMap[String, String]] = None,
    defaults: Option[Defaults] = None,
    `if`: Option[String] = None,
    outputs: Option[NonEmptyMap[String, String]] = None,
    timeoutMinutes: Option[Int] = None,
    continueOnError: Option[Either[Boolean, Expression]] = None,
    container: Option[Container] = None,
    services: Option[NonEmptyList[Service]] = None,
    strategy: Option[Strategy] = None,
    steps: NonEmptyList[Step]
)

object Job {

  implicit val JobEncoder: Encoder[Job] = job =>
    Json.obj(
      job.id.value := Json.obj(
        "name"              := job.name,
        "runs-on"           := job.runsOn,
        "needs"             := job.needs,
        "environment"       := job.environment,
        "env"               := job.env,
        "defaults"          := job.defaults,
        "if"                := job.`if`,
        "outputs"           := job.outputs,
        "timeout-minutes"   := job.timeoutMinutes,
        "continue-on-error" := job.continueOnError.map(_.fold(_.asJson, _.asJson)),
        "container"         := job.container,
        "services"          := job.services.map(_.map(_.asJson).reduce),
        "strategy"          := job.strategy
      )
    )

}
