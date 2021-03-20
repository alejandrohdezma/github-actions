package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `pull_request_review` event occurs. More than one activity type triggers this event.
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
  * @see [[https://developer.github.com/v3/pulls/reviews]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#pull-request-review-event-pull_request_review]]
  */
final case class PullRequestReview(types: List[PullRequestReview.Types]) extends Event {

  /** Launch this workflow when a pull request review is submitted into a non-pending state. */
  def onSubmitted() = copy(types = types :+ PullRequestReview.Types.Submitted)

  /** Launch this workflow when the body of a review has been edited. */
  def onEdited() = copy(types = types :+ PullRequestReview.Types.Edited)

  /** Launch this workflow when a review has been dismissed. */
  def onDismissed() = copy(types = types :+ PullRequestReview.Types.Dismissed)

}

/** Contains implicit values and classes relevant to [[PullRequestReview]]. */
object PullRequestReview {

  /** Allows converting a [[PullRequestReview]] value into [[yaml.Yaml yaml]]. */
  implicit val PullRequestReviewEncoder: Encoder[PullRequestReview] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[PullRequestReview]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A pull request review is submitted into a non-pending state. */
    case object Submitted extends Types(NonEmptyString.unsafe("submitted"))

    /** The body of a review has been edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** A review has been dismissed. */
    case object Dismissed extends Types(NonEmptyString.unsafe("dismissed"))

  }

}
