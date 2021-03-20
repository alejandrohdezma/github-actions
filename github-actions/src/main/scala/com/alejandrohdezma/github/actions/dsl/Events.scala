package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.base.Cron
import com.alejandrohdezma.github.actions.base.Ignore
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.base.NotEmptyList
import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.events

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Events {

  lazy val Event = events.Event
  type Event = events.Event

  lazy val checkRun = CheckRun(Nil)
  lazy val CheckRun = events.CheckRun
  type CheckRun = events.CheckRun

  lazy val checkSuite = CheckSuite(Nil)
  lazy val CheckSuite = events.CheckSuite
  type CheckSuite = events.CheckSuite

  lazy val create = Create
  lazy val Create = events.Create
  type Create = events.Create.type

  lazy val delete = Delete
  lazy val Delete = events.Delete
  type Delete = events.Delete.type

  lazy val deployment = Deployment
  lazy val Deployment = events.Deployment
  type Deployment = events.Deployment.type

  lazy val deploymentStatus = DeploymentStatus
  lazy val DeploymentStatus = events.DeploymentStatus
  type DeploymentStatus = events.DeploymentStatus.type

  lazy val fork = Fork
  lazy val Fork = events.Fork
  type Fork = events.Fork.type

  lazy val gollum = Gollum
  lazy val Gollum = events.Gollum
  type Gollum = events.Gollum.type

  lazy val issueComment = IssueComment(Nil)
  lazy val IssueComment = events.IssueComment
  type IssueComment = events.IssueComment

  lazy val issues = Issues(Nil)
  lazy val Issues = events.Issues
  type Issues = events.Issues

  lazy val label = Label(Nil)
  lazy val Label = events.Label
  type Label = events.Label

  lazy val member = Member(Nil)
  lazy val Member = events.Member
  type Member = events.Member

  lazy val milestone = Milestone(Nil)
  lazy val Milestone = events.Milestone
  type Milestone = events.Milestone

  lazy val pageBuild = PageBuild
  lazy val PageBuild = events.PageBuild
  type PageBuild = events.PageBuild.type

  lazy val project = Project(Nil)
  lazy val Project = events.Project
  type Project = events.Project

  lazy val projectCard = ProjectCard(Nil)
  lazy val ProjectCard = events.ProjectCard
  type ProjectCard = events.ProjectCard

  lazy val projectColumn = ProjectColumn(Nil)
  lazy val ProjectColumn = events.ProjectColumn
  type ProjectColumn = events.ProjectColumn

  lazy val public = Public
  lazy val Public = events.Public
  type Public = events.Public.type

  lazy val pullRequest = PullRequest(Nil)
  lazy val PullRequest = events.PullRequest
  type PullRequest = events.PullRequest

  lazy val pullRequestReview = PullRequestReview(Nil)
  lazy val PullRequestReview = events.PullRequestReview
  type PullRequestReview = events.PullRequestReview

  lazy val pullRequestReviewComment = PullRequestReviewComment(Nil)
  lazy val PullRequestReviewComment = events.PullRequestReviewComment
  type PullRequestReviewComment = events.PullRequestReviewComment

  lazy val pullRequestTarget = PullRequestTarget(Nil)
  lazy val PullRequestTarget = events.PullRequestTarget
  type PullRequestTarget = events.PullRequestTarget

  lazy val push = Push()
  lazy val Push = events.Push
  type Push = events.Push

  lazy val registryPackage = RegistryPackage(Nil)
  lazy val RegistryPackage = events.RegistryPackage
  type RegistryPackage = events.RegistryPackage

  lazy val release = Release(Nil)
  lazy val Release = events.Release
  type Release = events.Release

  lazy val status = Status
  lazy val Status = events.Status
  type Status = events.Status.type

  lazy val watch = Watch
  lazy val Watch = events.Watch
  type Watch = events.Watch.type

  lazy val workflowDispatch: WorkflowDispatch = WorkflowDispatch(Nil)
  lazy val WorkflowDispatch                   = events.WorkflowDispatch
  type WorkflowDispatch = events.WorkflowDispatch

  lazy val workflowRun = WorkflowRun.Builder(Nil, None)
  lazy val WorkflowRun = events.WorkflowRun
  type WorkflowRun = events.WorkflowRun

  lazy val repositoryDispatch = RepositoryDispatch(Nil)
  lazy val RepositoryDispatch = events.RepositoryDispatch
  type RepositoryDispatch = events.RepositoryDispatch

  def schedule(first: Cron, rest: Cron*): Schedule = Schedule(first, rest: _*)
  lazy val Schedule                                = events.Schedule
  type Schedule = events.Schedule

  def matching(first: NotEmptyString, rest: NotEmptyString*): Matching = Matching(NotEmptyList.of(first, rest: _*))
  def ignore(first: NotEmptyString, rest: NotEmptyString*): Ignore     = Ignore(NotEmptyList.of(first, rest: _*))

}
