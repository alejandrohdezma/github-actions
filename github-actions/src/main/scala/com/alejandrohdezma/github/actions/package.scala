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

package com.alejandrohdezma.github

import scala.reflect.ClassTag
import scala.reflect.classTag

import cats.data.NonEmptyList
import cats.kernel.Semigroup

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.types.time.Hour
import io.circe.Json

package object actions {

  lazy val workflow = Workflow.Builder1()

  /** Architectures */

  lazy val x64 = Architecture.x64
  type x64 = Architecture.x64.type

  lazy val ARM = Architecture.ARM
  type ARM = Architecture.ARM.type

  lazy val ARM64 = Architecture.ARM64
  type ARM64 = Architecture.ARM64.type

  /** Events */

  lazy val CheckRun = Event.CheckRun
  type CheckRun = Event.CheckRun

  lazy val CheckSuite = Event.CheckSuite
  type CheckSuite = Event.CheckSuite

  lazy val Create = Event.Create
  type Create = Event.Create.type

  lazy val Delete = Event.Delete
  type Delete = Event.Delete.type

  lazy val Deployment = Event.Deployment
  type Deployment = Event.Deployment.type

  lazy val DeploymentStatus = Event.DeploymentStatus
  type DeploymentStatus = Event.DeploymentStatus.type

  lazy val Fork = Event.Fork
  type Fork = Event.Fork.type

  lazy val Gollum = Event.Gollum
  type Gollum = Event.Gollum.type

  lazy val IssueComment = Event.IssueComment
  type IssueComment = Event.IssueComment

  lazy val Issues = Event.Issues
  type Issues = Event.Issues

  lazy val Label = Event.Label
  type Label = Event.Label

  lazy val Member = Event.Member
  type Member = Event.Member

  lazy val Milestone = Event.Milestone
  type Milestone = Event.Milestone

  lazy val PageBuild = Event.PageBuild
  type PageBuild = Event.PageBuild.type

  lazy val Project = Event.Project
  type Project = Event.Project

  lazy val ProjectCard = Event.ProjectCard
  type ProjectCard = Event.ProjectCard

  lazy val ProjectColumn = Event.ProjectColumn
  type ProjectColumn = Event.ProjectColumn

  lazy val Public = Event.Public
  type Public = Event.Public.type

  lazy val PullRequest = Event.PullRequest
  type PullRequest = Event.PullRequest

  lazy val PullRequestReview = Event.PullRequestReview
  type PullRequestReview = Event.PullRequestReview

  lazy val PullRequestReviewComment = Event.PullRequestReviewComment
  type PullRequestReviewComment = Event.PullRequestReviewComment

  lazy val PullRequestTarget = Event.PullRequestTarget
  type PullRequestTarget = Event.PullRequestTarget

  lazy val Push = Event.Push
  type Push = Event.Push

  lazy val RegistryPackage = Event.RegistryPackage
  type RegistryPackage = Event.RegistryPackage

  lazy val Release = Event.Release
  type Release = Event.Release

  lazy val Status = Event.Status
  type Status = Event.Status.type

  lazy val Watch = Event.Watch
  type Watch = Event.Watch.type

  lazy val WorkflowDispatch = Event.WorkflowDispatch
  type WorkflowDispatch = Event.WorkflowDispatch

  lazy val WorkflowRun = Event.WorkflowRun
  type WorkflowRun = Event.WorkflowRun

  lazy val RepositoryDispatch = Event.RepositoryDispatch
  type RepositoryDispatch = Event.RepositoryDispatch

  lazy val Schedule = Event.Schedule
  type Schedule = Event.Schedule

  /** Shells */

  lazy val Bash = Shell.Bash
  type Bash = Shell.Bash.type

  lazy val Pwsh = Shell.Pwsh
  type Pwsh = Shell.Pwsh.type

  lazy val Python = Shell.Python
  type Python = Shell.Python.type

  lazy val Sh = Shell.Sh
  type Sh = Shell.Sh.type

  lazy val Cmd = Shell.Cmd
  type Cmd = Shell.Cmd.type

  lazy val Powershell = Shell.Powershell
  type Powershell = Shell.Powershell.type

  def customShell(shell: NonEmptyString): Shell.Custom = Shell.Custom(shell)

  /** Runners */

  lazy val `macos-10.15` = Runner.HostedRunner.`macos-10.15`
  type `macos-10.15` = Runner.HostedRunner.`macos-10.15`.type

  lazy val `macos-11.0` = Runner.HostedRunner.`macos-11.0`
  type `macos-11.0` = Runner.HostedRunner.`macos-11.0`.type

  lazy val `macos-latest` = Runner.HostedRunner.`macos-latest`
  type `macos-latest` = Runner.HostedRunner.`macos-latest`.type

  lazy val `self-hosted` = Runner.HostedRunner.`self-hosted`
  type `self-hosted` = Runner.HostedRunner.`self-hosted`.type

  lazy val `ubuntu-16.04` = Runner.HostedRunner.`ubuntu-16.04`
  type `ubuntu-16.04` = Runner.HostedRunner.`ubuntu-16.04`.type

  lazy val `ubuntu-18.04` = Runner.HostedRunner.`ubuntu-18.04`
  type `ubuntu-18.04` = Runner.HostedRunner.`ubuntu-18.04`.type

  lazy val `ubuntu-20.04` = Runner.HostedRunner.`ubuntu-20.04`
  type `ubuntu-20.04` = Runner.HostedRunner.`ubuntu-20.04`.type

  lazy val `ubuntu-latest` = Runner.HostedRunner.`ubuntu-latest`
  type `ubuntu-latest` = Runner.HostedRunner.`ubuntu-latest`.type

  lazy val `windows-2016` = Runner.HostedRunner.`windows-2016`
  type `windows-2016` = Runner.HostedRunner.`windows-2016`.type

  lazy val `windows-2019` = Runner.HostedRunner.`windows-2019`
  type `windows-2019` = Runner.HostedRunner.`windows-2019`.type

  lazy val `windows-latest` = Runner.HostedRunner.`windows-latest`
  type `windows-latest` = Runner.HostedRunner.`windows-latest`.type

  /** Matrix */

  object matrix {

    def config(key: NonEmptyString)(first: NonEmptyString, rest: NonEmptyString*) =
      Strategy.matrix((key, NonEmptyList.of(first, rest: _*)))

  }

  /** Expressions */

  /**
   * Returns a expression that evaluates to `true` if the current event is the one provided.
   */
  def is[E <: Event: ClassTag] = {
    val eventName =
      classTag[E].runtimeClass
        .getSimpleName()
        .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
        .replaceAll("([a-z\\d])([A-Z])", "$1_$2")
        .toLowerCase()

    Expression(Refined.unsafeApply(s"github.event_name == '$eventName'"))
  }

  /**
   * Returns true when none of the previous steps have failed or been canceled.
   */
  lazy val success = Expression("success()")

  /**
   * Always returns true, even when canceled. A job or step will not run when a critical failure prevents the task from
   * running. For example, if getting sources failed.
   */
  lazy val always = Expression("always()")

  /**
   * Returns true if the workflow was canceled.
   */
  lazy val cancelled = Expression("cancelled()")

  /**
   * Returns true when any previous step of a job fails.
   */
  lazy val failure = Expression("failure()")

  implicit class ExpressionInterpolator(private val sc: StringContext) extends AnyVal {

    /**
     * Validates and transforms a literal string as a valid expression in compile time
     */
    def exp(args: Any*): Expression = macro Macros.expInterpolator

  }

  /** CRONS */

  /**
   * Cron that runs the workflow at 00:00 on January 1st every year.
   */
  lazy val yearly: String Refined Cron = "0 0 1 1 *"

  /**
   * Cron that runs the workflow at 00:00 on the first day of the month.
   */
  lazy val monthly: String Refined Cron = "0 0 1 * *"

  /**
   * Cron that runs the workflow at 00:00 every Sunday.
   */
  lazy val weekly: String Refined Cron = "0 0 * * 0"

  /**
   * Cron that runs the workflow at 00:00 everyday.
   */
  lazy val daily: String Refined Cron = "0 0 * * *"

  /**
   * Cron that runs the workflow at minute 0 of every hour.
   */
  lazy val hourly: String Refined Cron = "0 * * * *"

  /**
   * Cron that runs the workflow everyday at the provided hour.
   */
  def everydayAt(hour: Hour): String Refined Cron = Refined.unsafeApply(s"0 ${hour.value} * * *")

  /**
   * @see [[https://stackoverflow.com/a/57639657/4044345]]
   */
  type Cron = MatchesRegex[
    W.`"""^(((\\d+,)+\\d+|((\\d+|\\*)\\/\\d+|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)|(\\d+-\\d+)|\\d+|\\*|MON|TUE|WED|THU|FRI|SAT|SUN) ?){5,7}$"""`.T
  ]

  implicit private[actions] val JsonSemigroup: Semigroup[Json] = Semigroup.instance { case (a, b) => b.deepMerge(a) }

}
