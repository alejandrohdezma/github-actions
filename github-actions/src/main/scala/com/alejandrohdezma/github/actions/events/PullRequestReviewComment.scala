package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime a comment on a pull request's unified diff is modified, which triggers the
  * `pull_request_review_comment` event. More than one activity type triggers this event.
  *
  * Note: Workflows do not run on private base repositories when you open a pull request from a forked repository.
  *
  * When you create a pull request from a forked repository to the base repository, GitHub sends the `pull_request`
  * event to the base repository and no pull request events occur on the forked repository.
  *
  * Workflows don't run on forked repositories by default. You must enable GitHub Actions in the "Actions"" tab of the
  * forked repository.
  *
  * The permissions for the `GITHUB_TOKEN` in forked repositories is read-only. For more information about the
  * `GITHUB_TOKEN`, see [[https://help.github.com/en/articles/virtual-environments-for-github-actions]].
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/pulls/comments]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#pull-request-review-comment-event-pull_request_review_comment]]
  */
final case class PullRequestReviewComment(types: List[PullRequestReviewComment.Types]) extends Event {

  /** Launch this workflow when a comment is created. */
  def onCreated() = copy(types = types :+ PullRequestReviewComment.Types.Created)

  /** Launch this workflow when a comment is edited. */
  def onEdited() = copy(types = types :+ PullRequestReviewComment.Types.Edited)

  /** Launch this workflow when a comment is deleted. */
  def onDeleted() = copy(types = types :+ PullRequestReviewComment.Types.Deleted)

}

/** Contains implicit values and classes relevant to [[PullRequestReviewComment]]. */
object PullRequestReviewComment {

  /** Allows converting a [[PullRequestReviewComment]] value into [[yaml.Yaml yaml]]. */
  implicit val PullRequestReviewCommentEncoder: Encoder[PullRequestReviewComment] = event =>
    Yaml.obj("types" := event.types)

  /** The different types on which the [[PullRequestReviewComment]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A comment is created. */
    case object Created extends Types(NonEmptyString.unsafe("created"))

    /** A comment is edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** A comment is deleted. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
