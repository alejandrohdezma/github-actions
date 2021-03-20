package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.yaml._

/** Parent class for all the events for which a workflow can react.
  *
  * Available events are:
  *
  *   - [[CheckRun]] ([[actions.checkRun alias]] / [[actions$.checkRun:* default constructor]]).
  *   - [[CheckSuite]] ([[checkSuite alias]] / [[actions$.checkSuite:* default constructor]]).
  *   - [[Create]] ([[create alias]] / [[actions$.create:* default constructor]]).
  *   - [[Delete]] ([[delete alias]] / [[actions$.delete:* default constructor]]).
  *   - [[Deployment]] ([[deployment alias]] / [[actions$.deployment:* default constructor]]).
  *   - [[DeploymentStatus]] ([[deploymentStatus alias]] / [[actions$.deploymentStatus:* default constructor]]).
  *   - [[Fork]] ([[fork alias]] / [[actions$.fork:* default constructor]]).
  *   - [[Gollum]] ([[gollum alias]] / [[actions$.gollum:* default constructor]]).
  *   - [[IssueComment]] ([[issueComment alias]] / [[actions$.issueComment:* default constructor]]).
  *   - [[Issues]] ([[issues alias]] / [[actions$.issues:* default constructor]]).
  *   - [[Label]] ([[label alias]] / [[actions$.label:* default constructor]]).
  *   - [[Member]] ([[member alias]] / [[actions$.member:* default constructor]]).
  *   - [[Milestone]] ([[milestone alias]] / [[actions$.milestone:* default constructor]]).
  *   - [[PageBuild]] ([[pageBuild alias]] / [[actions$.pageBuild:* default constructor]]).
  *   - [[Project]] ([[project alias]] / [[actions$.project:* default constructor]]).
  *   - [[ProjectCard]] ([[projectCard alias]] / [[actions$.projectCard:* default constructor]]).
  *   - [[ProjectColumn]] ([[projectColumn alias]] / [[actions$.projectColumn:* default constructor]]).
  *   - [[Public]] ([[public alias]] / [[actions$.public:* default constructor]]).
  *   - [[PullRequest]] ([[pullRequest alias]] / [[actions$.pullRequest:* default constructor]]).
  *   - [[PullRequestReview]] ([[pullRequestReview alias]] / [[actions$.pullRequestReview:* default constructor]]).
  *   - [[PullRequestReviewComment]] ([[pullRequestReviewComment alias]] / [[actions$.pullRequestReviewComment:* default constructor]]).
  *   - [[PullRequestTarget]] ([[pullRequestTarget alias]] / [[actions$.pullRequestTarget:* default constructor]]).
  *   - [[Push]] ([[push alias]] / [[actions$.push:* default constructor]]).
  *   - [[RegistryPackage]] ([[registryPackage alias]] / [[actions$.registryPackage:* default constructor]]).
  *   - [[Release]] ([[release alias]] / [[actions$.release:* default constructor]]).
  *   - [[Status]] ([[status alias]] / [[actions$.status:* default constructor]]).
  *   - [[Watch]] ([[watch alias]] / [[actions$.watch:* default constructor]]).
  *   - [[WorkflowDispatch]] ([[workflowDispatch alias]] / [[actions$.workflowDispatch:* default constructor]]).
  *   - [[WorkflowRun]] ([[workflowRun alias]] / [[actions$.workflowRun:* default constructor]]).
  *   - [[RepositoryDispatch]] ([[repositoryDispatch alias]] / [[actions$.repositoryDispatch:* default constructor]]).
  *   - [[Schedule]] ([[schedule alias]] / [[actions$.schedule(* default constructor]]).
  *
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows]]
  */
abstract class Event private[events] {

  /** Check if this event is a [[CheckRun]]. */
  def isCheckRun(): Boolean = this match {
    case _: CheckRun => true
    case _           => false
  }

  /** Check if this event is a [[CheckSuite]]. */
  def isCheckSuite(): Boolean = this match {
    case _: CheckSuite => true
    case _             => false
  }

  /** Check if this event is a [[Create]]. */
  def isCreate(): Boolean = this match {
    case Create => true
    case _      => false
  }

  /** Check if this event is a [[Delete]]. */
  def isDelete(): Boolean = this match {
    case Delete => true
    case _      => false
  }

  /** Check if this event is a [[Deployment]]. */
  def isDeployment(): Boolean = this match {
    case Deployment => true
    case _          => false
  }

  /** Check if this event is a [[DeploymentStatus]]. */
  def isDeploymentStatus(): Boolean = this match {
    case DeploymentStatus => true
    case _                => false
  }

  /** Check if this event is a [[Fork]]. */
  def isFork(): Boolean = this match {
    case Fork => true
    case _    => false
  }

  /** Check if this event is a [[Gollum]]. */
  def isGollum(): Boolean = this match {
    case Gollum => true
    case _      => false
  }

  /** Check if this event is a [[IssueComment]]. */
  def isIssueComment(): Boolean = this match {
    case _: IssueComment => true
    case _               => false
  }

  /** Check if this event is a [[Issues]]. */
  def isIssues(): Boolean = this match {
    case _: Issues => true
    case _         => false
  }

  /** Check if this event is a [[Label]]. */
  def isLabel(): Boolean = this match {
    case _: Label => true
    case _        => false
  }

  /** Check if this event is a [[Member]]. */
  def isMember(): Boolean = this match {
    case _: Member => true
    case _         => false
  }

  /** Check if this event is a [[Milestone]]. */
  def isMilestone(): Boolean = this match {
    case _: Milestone => true
    case _            => false
  }

  /** Check if this event is a [[PageBuild]]. */
  def isPageBuild(): Boolean = this match {
    case PageBuild => true
    case _         => false
  }

  /** Check if this event is a [[Project]]. */
  def isProject(): Boolean = this match {
    case _: Project => true
    case _          => false
  }

  /** Check if this event is a [[ProjectCard]]. */
  def isProjectCard(): Boolean = this match {
    case _: ProjectCard => true
    case _              => false
  }

  /** Check if this event is a [[ProjectColumn]]. */
  def isProjectColumn(): Boolean = this match {
    case _: ProjectColumn => true
    case _                => false
  }

  /** Check if this event is a [[Public]]. */
  def isPublic(): Boolean = this match {
    case Public => true
    case _      => false
  }

  /** Check if this event is a [[PullRequest]]. */
  def isPullRequest(): Boolean = this match {
    case _: PullRequest => true
    case _              => false
  }

  /** Check if this event is a [[PullRequestReview]]. */
  def isPullRequestReview(): Boolean = this match {
    case _: PullRequestReview => true
    case _                    => false
  }

  /** Check if this event is a [[PullRequestReviewComment]]. */
  def isPullRequestReviewComment(): Boolean = this match {
    case _: PullRequestReviewComment => true
    case _                           => false
  }

  /** Check if this event is a [[PullRequestTarget]]. */
  def isPullRequestTarget(): Boolean = this match {
    case _: PullRequestTarget => true
    case _                    => false
  }

  /** Check if this event is a [[Push]]. */
  def isPush(): Boolean = this match {
    case _: Push => true
    case _       => false
  }

  /** Check if this event is a [[RegistryPackage]]. */
  def isRegistryPackage(): Boolean = this match {
    case _: RegistryPackage => true
    case _                  => false
  }

  /** Check if this event is a [[Release]]. */
  def isRelease(): Boolean = this match {
    case _: Release => true
    case _          => false
  }

  /** Check if this event is a [[Status]]. */
  def isStatus(): Boolean = this match {
    case Status => true
    case _      => false
  }

  /** Check if this event is a [[Watch]]. */
  def isWatch(): Boolean = this match {
    case Watch => true
    case _     => false
  }

  /** Check if this event is a [[WorkflowDispatch]]. */
  def isWorkflowDispatch(): Boolean = this match {
    case _: WorkflowDispatch => true
    case _                   => false
  }

  /** Check if this event is a [[WorkflowRun]]. */
  def isWorkflowRun(): Boolean = this match {
    case _: WorkflowRun => true
    case _              => false
  }

  /** Check if this event is a [[RepositoryDispatch]]. */
  def isRepositoryDispatch(): Boolean = this match {
    case _: RepositoryDispatch => true
    case _                     => false
  }

  /** Check if this event is a [[Schedule]]. */
  def isSchedule(): Boolean = this match {
    case _: Schedule => true
    case _           => false
  }

}

/** Contains implicit values that affect all events. */
object Event {

  /** Allows converting any [[Event]] value into [[yaml.Yaml yaml]]. */
  /** Allows converting a [[Event]] value into [[yaml.Yaml yaml]]. */
  implicit val EventEncoder: Encoder[Event] = {
    case e: CheckRun                 => Yaml.obj("check_run" := e)
    case e: CheckSuite               => Yaml.obj("check_suite" := e)
    case Create                      => Yaml.obj("create" := Yaml.empty)
    case Delete                      => Yaml.obj("delete" := Yaml.empty)
    case Deployment                  => Yaml.obj("deployment" := Yaml.empty)
    case DeploymentStatus            => Yaml.obj("deployment_status" := Yaml.empty)
    case Fork                        => Yaml.obj("fork" := Yaml.empty)
    case Gollum                      => Yaml.obj("gollum" := Yaml.empty)
    case e: IssueComment             => Yaml.obj("issue_comment" := e)
    case e: Issues                   => Yaml.obj("issues" := e)
    case e: Label                    => Yaml.obj("label" := e)
    case e: Member                   => Yaml.obj("member" := e)
    case e: Milestone                => Yaml.obj("milestone" := e)
    case PageBuild                   => Yaml.obj("page_build" := Yaml.empty)
    case e: Project                  => Yaml.obj("project" := e)
    case e: ProjectCard              => Yaml.obj("project_card" := e)
    case e: ProjectColumn            => Yaml.obj("project_column" := e)
    case Public                      => Yaml.obj("public" := Yaml.empty)
    case e: PullRequest              => Yaml.obj("pull_request" := e)
    case e: PullRequestReview        => Yaml.obj("pull_request_review" := e)
    case e: PullRequestReviewComment => Yaml.obj("pull_request_review_comment" := e)
    case e: PullRequestTarget        => Yaml.obj("pull_request_target" := e)
    case e: Push                     => Yaml.obj("push" := e)
    case e: RegistryPackage          => Yaml.obj("registry_package" := e)
    case e: Release                  => Yaml.obj("release" := e)
    case Status                      => Yaml.obj("status" := Yaml.empty)
    case Watch                       => Yaml.obj("watch" := Yaml.empty)
    case e: WorkflowDispatch         => Yaml.obj("workflow_dispatch" := e)
    case e: WorkflowRun              => Yaml.obj("workflow_run" := e)
    case e: RepositoryDispatch       => Yaml.obj("repository_dispatch" := e)
    case e: Schedule                 => Yaml.obj("schedule" := e)
  }

}
