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

import io.circe.Encoder
import io.circe.Json
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
    name: Option[String] = None,
    on: NonEmptyList[Event],
    env: Option[NonEmptyMap[String, String]] = None,
    defaults: Option[Defaults] = None,
    jobs: NonEmptyList[Job]
)

object Workflow {

  implicit lazy val WorkflowEncoder: Encoder[Workflow] = workflow =>
    Json.obj(
      "name"     := workflow.name,
      "on"       := workflow.on.map(_.asJson).reduce,
      "env"      := workflow.env,
      "defaults" := workflow.defaults,
      "jobs"     := workflow.jobs.map(_.asJson).reduce
    )

}
