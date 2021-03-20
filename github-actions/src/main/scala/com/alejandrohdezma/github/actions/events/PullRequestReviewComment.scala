package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

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
final case class PullRequestReviewComment(types: List[PullRequestReviewComment.Types]) extends Event {

  def onCreated() = copy(types = types :+ PullRequestReviewComment.Types.Created)

  def onEdited() = copy(types = types :+ PullRequestReviewComment.Types.Edited)

  def onDeleted() = copy(types = types :+ PullRequestReviewComment.Types.Deleted)

}

object PullRequestReviewComment {

  implicit val PullRequestReviewCommentEncoder: Encoder[PullRequestReviewComment] = event =>
    Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Created extends Types("created")
    case object Edited  extends Types("edited")
    case object Deleted extends Types("deleted")

  }

}
