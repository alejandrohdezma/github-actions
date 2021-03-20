package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.base.Cron
import com.alejandrohdezma.github.actions.base.Ignoring
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.base.NonEmptyList
import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.events

/** Contains aliases, default values and DSL constructors for the different [[events]]. */
@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Events {

  /** Default value for the [[events.CheckRun checkRun]] event. */
  lazy val checkRun = events.CheckRun(Nil)

  /** Alias for the [[events.CheckRun CheckRun]] type. */
  type checkRun = events.CheckRun

  /** Default value for the [[events.CheckSuite checkSuite]] event. */
  lazy val checkSuite = events.CheckSuite(Nil)

  /** Alias for the [[events.CheckSuite CheckSuite]] type. */
  type checkSuite = events.CheckSuite

  /** Default value for the [[events.Create create]] event. */
  lazy val create = events.Create

  /** Alias for the [[events.Create Create]] type. */
  type create = events.Create.type

  /** Default value for the [[events.Delete delete]] event. */
  lazy val delete = events.Delete

  /** Alias for the [[events.Delete Delete]] type. */
  type delete = events.Delete.type

  /** Default value for the [[events.Deployment deployment]] event. */
  lazy val deployment = events.Deployment

  /** Alias for the [[events.Deployment Deployment]] type. */
  type deployment = events.Deployment.type

  /** Default value for the [[events.DeploymentStatus deploymentStatus]] event. */
  lazy val deploymentStatus = events.DeploymentStatus

  /** Alias for the [[events.DeploymentStatus DeploymentStatus]] type. */
  type deploymentStatus = events.DeploymentStatus.type

  /** Default value for the [[events.Fork fork]] event. */
  lazy val fork = events.Fork

  /** Alias for the [[events.Fork Fork]] type. */
  type fork = events.Fork.type

  /** Default value for the [[events.Gollum gollum]] event. */
  lazy val gollum = events.Gollum

  /** Alias for the [[events.Gollum Gollum]] type. */
  type gollum = events.Gollum.type

  /** Default value for the [[events.IssueComment issueComment]] event. */
  lazy val issueComment = events.IssueComment(Nil)

  /** Alias for the [[events.IssueComment IssueComment]] type. */
  type issueComment = events.IssueComment

  /** Default value for the [[events.Issues issues]] event. */
  lazy val issues = events.Issues(Nil)

  /** Alias for the [[events.Issues Issues]] type. */
  type issues = events.Issues

  /** Default value for the [[events.Label label]] event. */
  lazy val label = events.Label(Nil)

  /** Alias for the [[events.Label Label]] type. */
  type label = events.Label

  /** Default value for the [[events.Member member]] event. */
  lazy val member = events.Member(Nil)

  /** Alias for the [[events.Member Member]] type. */
  type member = events.Member

  /** Default value for the [[events.Milestone milestone]] event. */
  lazy val milestone = events.Milestone(Nil)

  /** Alias for the [[events.Milestone Milestone]] type. */
  type milestone = events.Milestone

  /** Default value for the [[events.PageBuild pageBuild]] event. */
  lazy val pageBuild = events.PageBuild

  /** Alias for the [[events.PageBuild PageBuild]] type. */
  type pageBuild = events.PageBuild.type

  /** Default value for the [[events.Project project]] event. */
  lazy val project = events.Project(Nil)

  /** Alias for the [[events.Project Project]] type. */
  type project = events.Project

  /** Default value for the [[events.ProjectCard projectCard]] event. */
  lazy val projectCard = events.ProjectCard(Nil)

  /** Alias for the [[events.ProjectCard ProjectCard]] type. */
  type projectCard = events.ProjectCard

  /** Default value for the [[events.ProjectColumn projectColumn]] event. */
  lazy val projectColumn = events.ProjectColumn(Nil)

  /** Alias for the [[events.ProjectColumn ProjectColumn]] type. */
  type projectColumn = events.ProjectColumn

  /** Default value for the [[events.Public public]] event. */
  lazy val public = events.Public

  /** Alias for the [[events.Public Public]] type. */
  type public = events.Public.type

  /** Default value for the [[events.PullRequest pullRequest]] event. */
  lazy val pullRequest = events.PullRequest(Nil)

  /** Alias for the [[events.PullRequest PullRequest]] type. */
  type pullRequest = events.PullRequest

  /** Default value for the [[events.PullRequestReview pullRequestReview]] event. */
  lazy val pullRequestReview = events.PullRequestReview(Nil)

  /** Alias for the [[events.PullRequestReview PullRequestReview]] type. */
  type pullRequestReview = events.PullRequestReview

  /** Default value for the [[events.PullRequestReviewComment pullRequestReviewComment]] event. */
  lazy val pullRequestReviewComment = events.PullRequestReviewComment(Nil)

  /** Alias for the [[events.PullRequestReviewComment PullRequestReviewComment]] type. */
  type pullRequestReviewComment = events.PullRequestReviewComment

  /** Default value for the [[events.PullRequestTarget pullRequestTarget]] event. */
  lazy val pullRequestTarget = events.PullRequestTarget(Nil)

  /** Alias for the [[events.PullRequestTarget PullRequestTarget]] type. */
  type pullRequestTarget = events.PullRequestTarget

  /** Default value for the [[events.Push push]] event. */
  lazy val push = events.Push()

  /** Alias for the [[events.Push Push]] type. */
  type push = events.Push

  /** Default value for the [[events.RegistryPackage registryPackage]] event. */
  lazy val registryPackage = events.RegistryPackage(Nil)

  /** Alias for the [[events.RegistryPackage RegistryPackage]] type. */
  type registryPackage = events.RegistryPackage

  /** Default value for the [[events.Release release]] event. */
  lazy val release = events.Release(Nil)

  /** Alias for the [[events.Release Release]] type. */
  type release = events.Release

  /** Default value for the [[events.Status status]] event. */
  lazy val status = events.Status

  /** Alias for the [[events.Status Status]] type. */
  type status = events.Status.type

  /** Default value for the [[events.Watch watch]] event. */
  lazy val watch = events.Watch

  /** Alias for the [[events.Watch Watch]] type. */
  type watch = events.Watch.type

  /** Default value for the [[events.WorkflowDispatch workflowDispatch]] event. */
  lazy val workflowDispatch = events.WorkflowDispatch(Nil)

  /** Alias for the [[events.WorkflowDispatch WorkflowDispatch]] type. */
  type workflowDispatch = events.WorkflowDispatch

  /** Default value for the [[events.WorkflowRun workflowRun]] event. */
  lazy val workflowRun = events.WorkflowRun.Builder(Nil, None)

  /** Alias for the [[events.WorkflowRun WorkflowRun]] type. */
  type workflowRun = events.WorkflowRun

  /** Default value for the [[events.RepositoryDispatch repositoryDispatch]] event. */
  lazy val repositoryDispatch = events.RepositoryDispatch(Nil)

  /** Alias for the [[events.RepositoryDispatch RepositoryDispatch]] type. */
  type repositoryDispatch = events.RepositoryDispatch

  /** Returns a [[schedule]] event that runs the workflow given the provided crons.
    *
    * @example {{{schedule(daily, monthly, everydayAt(5))}}}
    */
  def schedule(first: Cron, rest: Cron*) = events.Schedule(NonEmptyList.of(first, rest: _*))

  /** Alias for the [[events.Schedule Schedule]] type. */
  type schedule = events.Schedule

  /** Returns a [[base.Matching matching]] glob patterns value that can be used on the following events:
    *
    *   - [[events.PullRequest.branches(matching* pull_request.branches]]
    *   - [[events.PullRequest.paths(matching* pull_request.paths]]
    *   - [[events.PullRequest.tags(matching* pull_request.tags]]
    *   - [[events.PullRequestTarget.branches(matching* pull_request_target.branches]]
    *   - [[events.PullRequestTarget.paths(matching* pull_request_target.paths]]
    *   - [[events.PullRequestTarget.tags(matching* pull_request_target.tags]]
    *   - [[events.Push.branches(matching* push.branches]]
    *   - [[events.Push.paths(matching* push.paths]]
    *   - [[events.Push.tags(matching* push.tags]]
    *   - [[events.WorkflowRun.branches(matching* workflow_run.branches]]
    */
  def matching(first: NonEmptyString, rest: NonEmptyString*) = Matching(NonEmptyList.of(first, rest: _*))

  /** Returns an [[base.Ignoring ignoring]] glob patterns value that can be used on the following events:
    *
    *   - [[events.PullRequest.branches(ignore* pull_request.branches-ignore]]
    *   - [[events.PullRequest.paths(ignore* pull_request.paths-ignore]]
    *   - [[events.PullRequest.tags(ignore* pull_request.tags-ignore]]
    *   - [[events.PullRequestTarget.branches(ignore* pull_request_target.branches-ignore]]
    *   - [[events.PullRequestTarget.paths(ignore* pull_request_target.paths-ignore]]
    *   - [[events.PullRequestTarget.tags(ignore* pull_request_target.tags-ignore]]
    *   - [[events.Push.branches(ignore* push.branches-ignore]]
    *   - [[events.Push.paths(ignore* push.paths-ignore]]
    *   - [[events.Push.tags(ignore* push.tags-ignore]]
    *   - [[events.WorkflowRun.branches(ignore* workflow_run.branches-ignore]]
    */
  def ignore(first: NonEmptyString, rest: NonEmptyString*) = Ignoring(NonEmptyList.of(first, rest: _*))

}
