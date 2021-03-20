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

import enumeratum.EnumEntry.Lowercase
import enumeratum.EnumEntry.Snakecase
import enumeratum._
import io.circe.Encoder
import io.circe.Json
import io.circe.refined._
import io.circe.syntax._

/**
 * https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows
 */
sealed abstract class Event(private[actions] encode: => Json) extends EnumEntry with Snakecase with Lowercase {

  def json = encode

  override def entryName: String = super.entryName.replaceAll("""(\w+)\(.*""", "$1")

}

object Event extends Enum[Event] with CatsEnum[Event] {

  implicit val EventEncoder: Encoder[Event] = event => Json.obj(event.entryName := event.json)

  /**
   * Runs your workflow anytime the check_run event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/checks/runs REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#check-run-event-check_run]]
   */
  final case class CheckRun(types: NonEmptyList[CheckRun.Types] = CheckRun.Types.defaults)
      extends Event(Json.obj("types" := types))

  object CheckRun {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Created         extends Types
      case object Rerequested     extends Types
      case object Completed       extends Types
      case object RequestedAction extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime the check_suite event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/checks/suites/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#check-suite-event-check_suite]]
   */
  final case class CheckSuite(types: NonEmptyList[CheckSuite.Types] = CheckSuite.Types.defaults)
      extends Event(Json.obj("types" := types))

  object CheckSuite {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Completed   extends Types
      case object Requested   extends Types
      case object Rerequested extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime someone creates a branch or tag, which triggers the create event.
   *
   * @see See more information about the [[https://developer.github.com/v3/git/refs/#create-a-reference REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#create-event-create]]
   */
  case object Create extends Event(Json.obj())

  /**
   * Runs your workflow anytime someone deletes a branch or tag, which triggers the delete event.
   *
   * @see See more information about the [[https://developer.github.com/v3/git/refs/#delete-a-reference REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#delete-event-delete]]
   */
  case object Delete extends Event(Json.obj())

  /**
   * Runs your workflow anytime someone creates a deployment, which triggers the deployment event. Deployments created
   * with a commit SHA may not have a Git ref.
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/deployments/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#deployment-event-deployment]]
   */
  case object Deployment extends Event(Json.obj())

  /**
   * Runs your workflow anytime a third party provides a deployment status, which triggers the deployment_status event.
   * Deployments created with a commit SHA may not have a Git ref.
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/deployments/#create-a-deployment-status REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#deployment-status-event-deployment_status]]
   */
  case object DeploymentStatus extends Event(Json.obj())

  /**
   * Runs your workflow anytime when someone forks a repository, which triggers the fork event.
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/forks/#create-a-fork REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#fork-event-fork]]
   */
  case object Fork extends Event(Json.obj())

  /**
   * Runs your workflow when someone creates or updates a Wiki page, which triggers the gollum event.
   *
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#gollum-event-gollum]]
   */
  case object Gollum extends Event(Json.obj())

  /**
   * Runs your workflow anytime the issue_comment event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/issues/comments/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#issue-comment-event-issue_comment]]
   */
  final case class IssueComment(types: NonEmptyList[IssueComment.Types] = IssueComment.Types.defaults)
      extends Event(Json.obj("types" := types))

  object IssueComment {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Created extends Types
      case object Edited  extends Types
      case object Deleted extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime the issues event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/issues REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#issues-event-issues]]
   */
  final case class Issues(types: NonEmptyList[Issues.Types] = Issues.Types.defaults)
      extends Event(Json.obj("types" := types))

  object Issues {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Opened       extends Types
      case object Edited       extends Types
      case object Deleted      extends Types
      case object Transferred  extends Types
      case object Pinned       extends Types
      case object Unpinned     extends Types
      case object Closed       extends Types
      case object Reopened     extends Types
      case object Assigned     extends Types
      case object Unassigned   extends Types
      case object Labeled      extends Types
      case object Unlabeled    extends Types
      case object Locked       extends Types
      case object Unlocked     extends Types
      case object Milestoned   extends Types
      case object Demilestoned extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime the label event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/issues/labels/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#label-event-label]]
   */
  final case class Label(types: NonEmptyList[Label.Types] = Label.Types.defaults)
      extends Event(Json.obj("types" := types))

  object Label {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Created extends Types
      case object Edited  extends Types
      case object Deleted extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime the member event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/collaborators/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#member-event-member]]
   */
  final case class Member(types: NonEmptyList[Member.Types] = Member.Types.defaults)
      extends Event(Json.obj("types" := types))

  object Member {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Added   extends Types
      case object Edited  extends Types
      case object Deleted extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime the milestone event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/issues/milestones/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#milestone-event-milestone]]
   */
  final case class Milestone(types: NonEmptyList[Milestone.Types] = Milestone.Types.defaults)
      extends Event(Json.obj("types" := types))

  object Milestone {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Created extends Types
      case object Closed  extends Types
      case object Opened  extends Types
      case object Edited  extends Types
      case object Deleted extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime someone pushes to a GitHub Pages-enabled branch, which triggers the page_build event.
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/pages/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#page-build-event-page_build]]
   */
  case object PageBuild extends Event(Json.obj())

  /**
   * Runs your workflow anytime the project event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/projects/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#project-event-project]]
   */
  final case class Project(types: NonEmptyList[Project.Types] = Project.Types.defaults)
      extends Event(Json.obj("types" := types))

  object Project {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Created  extends Types
      case object Updated  extends Types
      case object Closed   extends Types
      case object Reopened extends Types
      case object Edited   extends Types
      case object Deleted  extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime the project_card event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/projects/cards REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#project-card-event-project_card]]
   */
  final case class ProjectCard(types: NonEmptyList[ProjectCard.Types] = ProjectCard.Types.defaults)
      extends Event(Json.obj("types" := types))

  object ProjectCard {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Created   extends Types
      case object Moved     extends Types
      case object Converted extends Types
      case object Edited    extends Types
      case object Deleted   extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime the project_column event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/projects/columns REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#project-column-event-project_column]]
   */
  final case class ProjectColumn(types: NonEmptyList[ProjectColumn.Types] = ProjectColumn.Types.defaults)
      extends Event(Json.obj("types" := types))

  object ProjectColumn {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Created extends Types
      case object Updated extends Types
      case object Moved   extends Types
      case object Deleted extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime someone makes a private repository public, which triggers the public event.
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/#edit REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#public-event-public]]
   */
  case object Public extends Event(Json.obj())

  /**
   * Runs your workflow anytime the pull_request event occurs. More than one activity type triggers this event.
   *
   * Note: Workflows do not run on private base repositories when you open a pull request from a forked repository.
   *
   * When you create a pull request from a forked repository to the base repository, GitHub sends the pull_request
   * event to the base repository and no pull request events occur on the forked repository.
   *
   * Workflows don't run on forked repositories by default. You must enable GitHub Actions in the Actions tab of the
   * forked repository.
   *
   * The permissions for the GITHUB_TOKEN in forked repositories is read-only. For more information about the
   * GITHUB_TOKEN, see https://help.github.com/en/articles/virtual-environments-for-github-actions.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/pulls REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#pull-request-event-pull_request]]
   */
  final case class PullRequest(
      types: NonEmptyList[PullRequest.Types] = PullRequest.Types.defaults,
      branches: Option[Branches] = None,
      tags: Option[Tags] = None,
      paths: Option[Paths] = None
  ) extends Event(Json.obj("types" := types).deepMerge(branches.asJson).deepMerge(tags.asJson).deepMerge(paths.asJson))

  object PullRequest {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Assigned             extends Types
      case object Unassigned           extends Types
      case object Labeled              extends Types
      case object Unlabeled            extends Types
      case object Opened               extends Types
      case object Edited               extends Types
      case object Closed               extends Types
      case object Reopened             extends Types
      case object Synchronize          extends Types
      case object ReadyForReview       extends Types
      case object Locked               extends Types
      case object Unlocked             extends Types
      case object ReviewRequested      extends Types
      case object ReviewRequestRemoved extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.of(Opened, Synchronize, Reopened)

    }

  }

  /**
   * Runs your workflow anytime the pull_request_review event occurs. More than one activity type triggers this event.
   *
   * Note: Workflows do not run on private base repositories when you open a pull request from a forked repository.
   *
   * When you create a pull request from a forked repository to the base repository, GitHub sends the pull_request event
   * to the base repository and no pull request events occur on the forked repository.
   *
   * Workflows don't run on forked
   * repositories by default. You must enable GitHub Actions in the Actions tab of the forked repository.
   *
   * The permissions for the GITHUB_TOKEN in forked repositories is read-only. For more information about the
   * GITHUB_TOKEN, see https://help.github.com/en/articles/virtual-environments-for-github-actions.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/pulls/reviews REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#pull-request-review-event-pull_request_review]]
   */
  final case class PullRequestReview(types: NonEmptyList[PullRequestReview.Types] = PullRequestReview.Types.defaults)
      extends Event(Json.obj("types" := types))

  object PullRequestReview {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Submitted extends Types
      case object Edited    extends Types
      case object Dismissed extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime a comment on a pull request's unified diff is modified, which triggers the
   * pull_request_review_comment event. More than one activity type triggers this event.
   *
   * Note: Workflows do not run on private base repositories when you open a pull request from a forked repository.
   *
   * When you create a pull request from a forked repository to the base repository, GitHub sends the pull_request event
   * to the base repository and no pull request events occur on the forked repository.
   *
   * Workflows don't run on forked repositories by default. You must enable GitHub Actions in the Actions tab of the
   * forked repository.
   *
   * The permissions for the GITHUB_TOKEN in forked repositories is read-only. For more information
   * about the GITHUB_TOKEN, see https://help.github.com/en/articles/virtual-environments-for-github-actions.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/pulls/comments REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#pull-request-review-comment-event-pull_request_review_comment]]
   */
  final case class PullRequestReviewComment(
      types: NonEmptyList[PullRequestReviewComment.Types] = PullRequestReviewComment.Types.defaults
  ) extends Event(Json.obj("types" := types))

  object PullRequestReviewComment {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Created extends Types
      case object Edited  extends Types
      case object Deleted extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * This event is similar to pull_request, except that it runs in the context of the base repository of the pull
   * request, rather than in the merge commit. This means that you can more safely make your secrets available to the
   * workflows triggered by the pull request, because only workflows defined in the commit on the base repository are
   * run. For example, this event allows you to create workflows that label and comment on pull requests, based on the
   * contents of the event payload.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see [[https://docs.github.com/en/actions/reference/events-that-trigger-workflows#pull_request_target]]
   */
  final case class PullRequestTarget(
      types: NonEmptyList[PullRequestTarget.Types] = PullRequestTarget.Types.defaults,
      branches: Option[Branches] = None,
      tags: Option[Tags] = None,
      paths: Option[Paths] = None
  ) extends Event(Json.obj("types" := types).deepMerge(branches.asJson).deepMerge(tags.asJson).deepMerge(paths.asJson))

  object PullRequestTarget {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Assigned             extends Types
      case object Unassigned           extends Types
      case object Labeled              extends Types
      case object Unlabeled            extends Types
      case object Opened               extends Types
      case object Edited               extends Types
      case object Closed               extends Types
      case object Reopened             extends Types
      case object Synchronize          extends Types
      case object ReadyForReview       extends Types
      case object Locked               extends Types
      case object Unlocked             extends Types
      case object ReviewRequested      extends Types
      case object ReviewRequestRemoved extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.of(Opened, Synchronize, Reopened)

    }

  }

  /**
   * Runs your workflow when someone pushes to a repository branch, which triggers the push event.
   *
   * Note: The webhook payload available to GitHub Actions does not include the added, removed, and modified attributes
   * in the commit object. You can retrieve the full commit object using the REST API.
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/commits/#get-a-single-commit REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#push-event-push]]
   */
  final case class Push(branches: Option[Branches] = None, tags: Option[Tags] = None, paths: Option[Paths] = None)
      extends Event(branches.asJson.deepMerge(tags.asJson).deepMerge(paths.asJson))

  /**
   * Runs your workflow anytime a package is published or updated.
   *
   * @see [[https://help.github.com/en/github/managing-packages-with-github-packages]]
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see [[https://help.github.com/en/actions/reference/events-that-trigger-workflows#registry-package-event-registry_package]]
   */
  final case class RegistryPackage(types: NonEmptyList[RegistryPackage.Types] = RegistryPackage.Types.defaults)
      extends Event(Json.obj("types" := types))

  object RegistryPackage {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Published extends Types
      case object Updated   extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime the release event occurs. More than one activity type triggers this event.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/releases/ in the GitHub Developer documentation REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#release-event-release]]
   */
  final case class Release(types: NonEmptyList[Release.Types] = Release.Types.defaults)
      extends Event(Json.obj("types" := types))

  object Release {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Published   extends Types
      case object Unpublished extends Types
      case object Created     extends Types
      case object Edited      extends Types
      case object Deleted     extends Types
      case object Prereleased extends Types
      case object Released    extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * Runs your workflow anytime the status of a Git commit changes, which triggers the status event.
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/statuses/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#status-event-status]]
   */
  case object Status extends Event(Json.obj())

  /**
   * Runs your workflow anytime the watch event occurs. More than one activity type triggers this event.
   *
   * @see See more information about the [[https://developer.github.com/v3/activity/starring/ REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#watch-event-watch]]
   */
  case object Watch extends Event(Json.obj())

  /**
   * You can now create workflows that are manually triggered with the new workflow_dispatch event. You will then see a
   * 'Run workflow' button on the Actions tab, enabling you to easily trigger a run.
   *
   * @param inputs input parameters allow you to specify data that the action expects to use during runtime. GitHub
   *   stores input parameters as environment variables. Input ids with uppercase letters are converted to lowercase
   *   during runtime. We recommended using lowercase input ids.
   *
   * @see [[https://github.blog/changelog/2020-07-06-github-actions-manual-triggers-with-workflow_dispatch/]]
   */
  final case class WorkflowDispatch(inputs: Input*) extends Event(inputs.map(_.asJson).reduce(JsonSemigroup.combine))

  /**
   * This event occurs when a workflow run is requested or completed, and allows you to execute a workflow based on the
   * finished result of another workflow. For example, if your pull_request workflow generates build artifacts, you can
   * create a new workflow that uses workflow_run to analyze the results and add a comment to the original pull request.
   *
   * @param types selects the types of activity that will trigger a workflow run.
   *
   * @see [[https://docs.github.com/en/actions/reference/events-that-trigger-workflows#workflow_run]]
   */
  final case class WorkflowRun(
      types: NonEmptyList[WorkflowRun.Types] = WorkflowRun.Types.defaults,
      workflows: NonEmptyList[String],
      branches: Option[Branches]
  ) extends Event(Json.obj("types" := types, "workflows" := workflows).deepMerge(branches.asJson))

  object WorkflowRun {

    sealed trait Types extends EnumEntry with EnumEntry.Snakecase with EnumEntry.Lowercase

    object Types extends Enum[Types] with CirceEnum[Types] with CatsEnum[Types] {

      case object Requested extends Types
      case object Completed extends Types

      val values = findValues

      val defaults: NonEmptyList[Types] = NonEmptyList.fromListUnsafe(values.toList)

    }

  }

  /**
   * You can use the GitHub API to trigger a webhook event called repository_dispatch when you want to trigger a
   * workflow for activity that happens outside of GitHub.
   *
   * To trigger the custom repository_dispatch webhook event, you must send a POST request to a GitHub API endpoint and
   * provide an event_type name to describe the activity type. To trigger a workflow run, you must also configure your
   * workflow to use the repository_dispatch event.
   *
   * @param types the event_type under which the repository_dispatch should trigger this workflow
   *
   * @see See more information about the [[https://developer.github.com/v3/repos/#create-a-repository-dispatch-event REST API]].
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#external-events-repository_dispatch]]
   */
  final case class RepositoryDispatch(types: String*) extends Event(Json.obj("types" := types))

  /**
   * You can schedule a workflow to run at specific UTC times using POSIX
   * [[https://pubs.opengroup.org/onlinepubs/9699919799/utilities/crontab.html#tag_20_25_07 cron syntax]]. Scheduled
   * workflows run on the latest commit on the default or base branch. The shortest interval you can run scheduled
   * workflows is once every 5 minutes.
   *
   * Note: GitHub Actions does not support the non-standard syntax @yearly, @monthly, @weekly, @daily, @hourly, and
   * @reboot.
   *
   * You can use crontab guru (https://crontab.guru/). to help generate your cron syntax and confirm what time it will
   * run. To help you get started, there is also a list of crontab guru examples (https://crontab.guru/examples.html)
   *
   * @param crons the list of cron patterns when the workflow should run
   * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#scheduled-events-schedule]]
   */
  final case class Schedule(crons: NonEmptyList[Cron]) extends Event(crons.map(cron => Json.obj("cron" := cron)).asJson)

  val values = findValues

}
