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

import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Encoder
import io.circe.Json
import io.circe.refined._
import io.circe.syntax._

/**
 * @param name the name of your workflow. GitHub displays the names of your workflows on your repository's actions
 *   page. If you omit this field, GitHub sets the name to the workflow's filename.
 * @param on the name of the GitHub event that triggers the workflow. You can provide a single event string, array of
 *   events, array of event types, or an event configuration map that schedules a workflow or restricts the execution
 *   of a workflow to specific files, tags, or branch changes. For a list of available events,
 *   see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows]].
 * @param env a map of environment variables that are available to all jobs and steps in the workflow
 * @param defaults a map of default settings that will apply to all jobs in the workflow
 * @param jobs the list of jobs that conform a workflow
 *
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions]]
 */
final case class Workflow(
    name: Option[NonEmptyString] = None,
    on: NonEmptyList[Event],
    env: Option[NonEmptyMap[NonEmptyString, NonEmptyString]] = None,
    defaults: Option[Defaults] = None,
    jobs: NonEmptyList[Job]
) {

  def job(job: Job): Workflow = copy(jobs = jobs.append(job))

}

object Workflow {

  final case class Builder1(
      name: Option[NonEmptyString] = None,
      env: Option[NonEmptyMap[NonEmptyString, NonEmptyString]] = None,
      defaults: Option[Defaults] = None
  ) {

    def name(name: NonEmptyString): Builder1 = copy(name = Some(name))

    def env(key: NonEmptyString, value: NonEmptyString): Builder1 =
      copy(env = env.map(_.add((key, value))).orElse(Some(NonEmptyMap.of((key, value)))))

    def defaults(shell: Shell, workingDirectory: NonEmptyString): Builder1 =
      copy(defaults = Some(Defaults(Some(shell), Some(workingDirectory))))

    def defaults(shell: Shell): Builder1 =
      copy(defaults = defaults.map(_.copy(shell = Some(shell))).orElse(Some(Defaults(shell = Some(shell)))))

    def defaults(workingDirectory: NonEmptyString): Builder1 =
      copy(defaults =
        defaults
          .map(_.copy(workingDirectory = Some(workingDirectory)))
          .orElse(Some(Defaults(workingDirectory = Some(workingDirectory))))
      )

    def on(first: Event, rest: Event*): Builder2 =
      Builder2(name = name, env = env, defaults = defaults, on = NonEmptyList.of(first, rest: _*))

  }

  final case class Builder2(
      name: Option[NonEmptyString] = None,
      on: NonEmptyList[Event],
      env: Option[NonEmptyMap[NonEmptyString, NonEmptyString]] = None,
      defaults: Option[Defaults] = None
  ) {

    def name(name: NonEmptyString): Builder2 = copy(name = Some(name))

    def env(key: NonEmptyString, value: NonEmptyString): Builder2 =
      copy(env = env.map(_.add((key, value))).orElse(Some(NonEmptyMap.of((key, value)))))

    def defaults(shell: Shell, workingDirectory: NonEmptyString): Builder2 =
      copy(defaults = Some(Defaults(Some(shell), Some(workingDirectory))))

    def defaults(shell: Shell): Builder2 =
      copy(defaults = defaults.map(_.copy(shell = Some(shell))).orElse(Some(Defaults(shell = Some(shell)))))

    def defaults(workingDirectory: NonEmptyString): Builder2 =
      copy(defaults =
        defaults
          .map(_.copy(workingDirectory = Some(workingDirectory)))
          .orElse(Some(Defaults(workingDirectory = Some(workingDirectory))))
      )

    def on(first: Event, rest: Event*) =
      copy(on = on ::: NonEmptyList.of(first, rest: _*))

    def job(job: Job): Workflow =
      Workflow(name = name, on = on, env = env, defaults = defaults, jobs = NonEmptyList.one(job))

  }

  implicit lazy val WorkflowEncoder: Encoder[Workflow] = workflow =>
    Json.obj(
      "name"     := workflow.name,
      "on"       := workflow.on.map(_.asJson).reduce,
      "env"      := workflow.env,
      "defaults" := workflow.defaults,
      "jobs"     := workflow.jobs.map(_.asJson).reduce
    )

}
