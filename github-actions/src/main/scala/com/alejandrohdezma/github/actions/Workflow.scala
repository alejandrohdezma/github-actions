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

import com.alejandrohdezma.github.actions.base.Expression
import com.alejandrohdezma.github.actions.base.Id
import com.alejandrohdezma.github.actions.base.NotEmptyList
import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

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
    name: Option[NotEmptyString] = None,
    on: NotEmptyList[Event],
    env: Map[NotEmptyString, NotEmptyString] = ListMap(),
    defaults: Option[Defaults] = None,
    jobs: NotEmptyList[Job]
) {

  def name(name: NotEmptyString): Workflow = copy(name = Some(name))

  def env(key: NotEmptyString, value: NotEmptyString): Workflow = copy(env = env + ((key, value)))

  def env(key: NotEmptyString, value: Expression): Workflow = env(key, value.show())

  def disableDefaults(): Workflow = copy(defaults = None)

  def defaults(shell: Shell, workingDirectory: NotEmptyString): Workflow =
    copy(defaults = Some(Defaults(Some(shell), Some(workingDirectory))))

  def defaults(shell: Shell): Workflow =
    copy(defaults = defaults.map(_.copy(shell = Some(shell))).orElse(Some(Defaults(shell = Some(shell)))))

  def defaults(workingDirectory: NotEmptyString): Workflow =
    copy(defaults =
      defaults
        .map(_.copy(workingDirectory = Some(workingDirectory)))
        .orElse(Some(Defaults(workingDirectory = Some(workingDirectory))))
    )

  def on(first: Event, rest: Event*): Workflow = copy(on = on ::: NotEmptyList.of(first, rest: _*))

  def jobs(first: Job, rest: Job*): Workflow = copy(jobs = jobs ::: NotEmptyList.of(first, rest: _*))

  def job(job: Job): Workflow = jobs(job)

  def job(id: Id)(builder: Job.Builder => Job): Workflow = jobs(builder(Job.Builder(id)))

}

object Workflow {

  final case class Builder1(
      name: Option[NotEmptyString] = None,
      env: Map[NotEmptyString, NotEmptyString] = ListMap(),
      defaults: Option[Defaults] = None
  ) {

    def name(name: NotEmptyString): Builder1 = copy(name = Some(name))

    def env(key: NotEmptyString, value: NotEmptyString): Builder1 = copy(env = env + ((key, value)))

    def env(key: NotEmptyString, value: Expression): Builder1 = env(key, value.show())

    def disableDefaults(): Builder1 = copy(defaults = None)

    def defaults(shell: Shell, workingDirectory: NotEmptyString): Builder1 =
      copy(defaults = Some(Defaults(Some(shell), Some(workingDirectory))))

    def defaults(shell: Shell): Builder1 =
      copy(defaults = defaults.map(_.copy(shell = Some(shell))).orElse(Some(Defaults(shell = Some(shell)))))

    def defaults(workingDirectory: NotEmptyString): Builder1 =
      copy(defaults =
        defaults
          .map(_.copy(workingDirectory = Some(workingDirectory)))
          .orElse(Some(Defaults(workingDirectory = Some(workingDirectory))))
      )

    def on(first: Event, rest: Event*): Builder2 =
      Builder2(name = name, env = env, defaults = defaults, on = NotEmptyList.of(first, rest: _*))

  }

  final case class Builder2(
      name: Option[NotEmptyString] = None,
      on: NotEmptyList[Event],
      env: Map[NotEmptyString, NotEmptyString] = ListMap(),
      defaults: Option[Defaults] = None
  ) {

    def name(name: NotEmptyString): Builder2 = copy(name = Some(name))

    def env(key: NotEmptyString, value: NotEmptyString): Builder2 = copy(env = env + ((key, value)))

    def env(key: NotEmptyString, value: Expression): Builder2 = env(key, value.show())

    def disableDefaults(): Builder2 = copy(defaults = None)

    def defaults(shell: Shell, workingDirectory: NotEmptyString): Builder2 =
      copy(defaults = Some(Defaults(Some(shell), Some(workingDirectory))))

    def defaults(shell: Shell): Builder2 =
      copy(defaults = defaults.map(_.copy(shell = Some(shell))).orElse(Some(Defaults(shell = Some(shell)))))

    def defaults(workingDirectory: NotEmptyString): Builder2 =
      copy(defaults =
        defaults
          .map(_.copy(workingDirectory = Some(workingDirectory)))
          .orElse(Some(Defaults(workingDirectory = Some(workingDirectory))))
      )

    def on(first: Event, rest: Event*): Builder2 = copy(on = on ::: NotEmptyList.of(first, rest: _*))

    def jobs(first: Job, rest: Job*): Workflow =
      Workflow(name = name, on = on, env = env, defaults = defaults, jobs = NotEmptyList.of(first, rest: _*))

    def job(job: Job): Workflow = jobs(job)

    def job(id: Id)(builder: Job.Builder => Job): Workflow = job(builder(Job.Builder(id)))

  }

  implicit lazy val WorkflowEncoder: Encoder[Workflow] = workflow =>
    Yaml.obj(
      "name"     := workflow.name,
      "on"       := workflow.on.map(_.asYaml).reduce((a, b) => a.merge(b)),
      "env"      := (if (workflow.env.isEmpty) Yaml.Null else workflow.env.map(t => t._1.value -> t._2).asYaml),
      "defaults" := workflow.defaults,
      "jobs"     := workflow.jobs.map(_.asYaml).reduce((a, b) => b.merge(a))
    )

}
