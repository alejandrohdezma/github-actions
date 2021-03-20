package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.yaml._

/**
 * Parent class for all the events for which a workflow can react.
 *
 * Available events are:
 *
 * - [[CheckRun]]: [[checkRun default constructor]].
 * - [[CheckSuite]]: [[checkSuite default constructor]].
 * - [[Create]]: [[create default constructor]].
 * - [[Delete]]: [[delete default constructor]].
 * - [[Deployment]]: [[deployment default constructor]].
 * - [[DeploymentStatus]]: [[deploymentStatus default constructor]].
 * - [[Fork]]: [[fork default constructor]].
 * - [[Gollum]]: [[gollum default constructor]].
 * - [[IssueComment]]: [[issueComment default constructor]].
 * - [[Issues]]: [[issues default constructor]].
 * - [[Label]]: [[label default constructor]].
 * - [[Member]]: [[member default constructor]].
 * - [[Milestone]]: [[milestone default constructor]].
 * - [[PageBuild]]: [[pageBuild default constructor]].
 * - [[Project]]: [[project default constructor]].
 * - [[ProjectCard]]: [[projectCard default constructor]].
 * - [[ProjectColumn]]: [[projectColumn default constructor]].
 * - [[Public]]: [[public default constructor]].
 * - [[PullRequest]]: [[pullRequest default constructor]].
 * - [[PullRequestReview]]: [[pullRequestReview default constructor]].
 * - [[PullRequestReviewComment]]: [[pullRequestReviewComment default constructor]].
 * - [[PullRequestTarget]]: [[pullRequestTarget default constructor]].
 * - [[Push]]: [[push default constructor]].
 * - [[RegistryPackage]]: [[registryPackage default constructor]].
 * - [[Release]]: [[release default constructor]].
 * - [[Status]]: [[status default constructor]].
 * - [[Watch]]: [[watch default constructor]].
 * - [[WorkflowDispatch]]: [[workflowDispatch default constructor]].
 * - [[WorkflowRun]]: [[workflowRun default constructor]].
 * - [[RepositoryDispatch]]: [[repositoryDispatch default constructor]].
 * - [[Schedule]]: [[schedule default constructor]].
 *
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows]]
 */
abstract class Event private[events] {

  def isCheckRun(): Boolean = this match {
    case _: CheckRun => true
    case _           => false
  }

  def isCheckSuite(): Boolean = this match {
    case _: CheckSuite => true
    case _             => false
  }

  def isCreate(): Boolean = this match {
    case Create => true
    case _      => false
  }

  def isDelete(): Boolean = this match {
    case Delete => true
    case _      => false
  }

  def isDeployment(): Boolean = this match {
    case Deployment => true
    case _          => false
  }

  def isDeploymentStatus(): Boolean = this match {
    case DeploymentStatus => true
    case _                => false
  }

  def isFork(): Boolean = this match {
    case Fork => true
    case _    => false
  }

  def isGollum(): Boolean = this match {
    case Gollum => true
    case _      => false
  }

  def isIssueComment(): Boolean = this match {
    case _: IssueComment => true
    case _               => false
  }

  def isIssues(): Boolean = this match {
    case _: Issues => true
    case _         => false
  }

  def isLabel(): Boolean = this match {
    case _: Label => true
    case _        => false
  }

  def isMember(): Boolean = this match {
    case _: Member => true
    case _         => false
  }

  def isMilestone(): Boolean = this match {
    case _: Milestone => true
    case _            => false
  }

  def isPageBuild(): Boolean = this match {
    case PageBuild => true
    case _         => false
  }

  def isProject(): Boolean = this match {
    case _: Project => true
    case _          => false
  }

  def isProjectCard(): Boolean = this match {
    case _: ProjectCard => true
    case _              => false
  }

  def isProjectColumn(): Boolean = this match {
    case _: ProjectColumn => true
    case _                => false
  }

  def isPublic(): Boolean = this match {
    case Public => true
    case _      => false
  }

  def isPullRequest(): Boolean = this match {
    case _: PullRequest => true
    case _              => false
  }

  def isPullRequestReview(): Boolean = this match {
    case _: PullRequestReview => true
    case _                    => false
  }

  def isPullRequestReviewComment(): Boolean = this match {
    case _: PullRequestReviewComment => true
    case _                           => false
  }

  def isPullRequestTarget(): Boolean = this match {
    case _: PullRequestTarget => true
    case _                    => false
  }

  def isPush(): Boolean = this match {
    case _: Push => true
    case _       => false
  }

  def isRegistryPackage(): Boolean = this match {
    case _: RegistryPackage => true
    case _                  => false
  }

  def isRelease(): Boolean = this match {
    case _: Release => true
    case _          => false
  }

  def isStatus(): Boolean = this match {
    case Status => true
    case _      => false
  }

  def isWatch(): Boolean = this match {
    case Watch => true
    case _     => false
  }

  def isWorkflowDispatch(): Boolean = this match {
    case _: WorkflowDispatch => true
    case _                   => false
  }

  def isWorkflowRun(): Boolean = this match {
    case _: WorkflowRun => true
    case _              => false
  }

  def isRepositoryDispatch(): Boolean = this match {
    case _: RepositoryDispatch => true
    case _                     => false
  }

  def isSchedule(): Boolean = this match {
    case _: Schedule => true
    case _           => false
  }

}

object Event {

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
