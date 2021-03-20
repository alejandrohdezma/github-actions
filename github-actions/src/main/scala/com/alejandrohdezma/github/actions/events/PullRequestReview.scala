package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

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
final case class PullRequestReview(types: List[PullRequestReview.Types]) extends Event {

  def onSubmitted() = copy(types = types :+ PullRequestReview.Types.Submitted)

  def onEdited() = copy(types = types :+ PullRequestReview.Types.Edited)

  def onDismissed() = copy(types = types :+ PullRequestReview.Types.Dismissed)

}

object PullRequestReview {

  implicit val PullRequestReviewEncoder: Encoder[PullRequestReview] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Submitted extends Types("submitted")
    case object Edited    extends Types("edited")
    case object Dismissed extends Types("dismissed")

  }

}
