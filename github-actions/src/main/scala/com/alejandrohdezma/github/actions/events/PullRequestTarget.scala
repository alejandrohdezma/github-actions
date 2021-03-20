package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.Branches
import com.alejandrohdezma.github.actions.NotEmptyString
import com.alejandrohdezma.github.actions.Paths
import com.alejandrohdezma.github.actions.Tags
import com.alejandrohdezma.github.actions.yaml._

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
    types: List[PullRequestTarget.Types],
    branches: Option[Branches] = None,
    tags: Option[Tags] = None,
    paths: Option[Paths] = None
) extends Event {

  def onAssigned() = copy(types = types :+ PullRequestTarget.Types.Assigned)

  def onUnassigned() = copy(types = types :+ PullRequestTarget.Types.Unassigned)

  def onLabeled() = copy(types = types :+ PullRequestTarget.Types.Labeled)

  def onUnlabeled() = copy(types = types :+ PullRequestTarget.Types.Unlabeled)

  def onOpened() = copy(types = types :+ PullRequestTarget.Types.Opened)

  def onEdited() = copy(types = types :+ PullRequestTarget.Types.Edited)

  def onClosed() = copy(types = types :+ PullRequestTarget.Types.Closed)

  def onReopened() = copy(types = types :+ PullRequestTarget.Types.Reopened)

  def onSynchronize() = copy(types = types :+ PullRequestTarget.Types.Synchronize)

  def onReadyForReview() = copy(types = types :+ PullRequestTarget.Types.ReadyForReview)

  def onLocked() = copy(types = types :+ PullRequestTarget.Types.Locked)

  def onUnlocked() = copy(types = types :+ PullRequestTarget.Types.Unlocked)

  def onReviewRequested() = copy(types = types :+ PullRequestTarget.Types.ReviewRequested)

  def onReviewRequestRemoved() = copy(types = types :+ PullRequestTarget.Types.ReviewRequestRemoved)

}

object PullRequestTarget {

  implicit val PullRequestTargetEncoder: Encoder[PullRequestTarget] = event =>
    Yaml
      .obj("types" := event.types)
      .merge(event.branches.asYaml)
      .merge(event.tags.asYaml)
      .merge(event.paths.asYaml)

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Assigned             extends Types("assigned")
    case object Unassigned           extends Types("unassigned")
    case object Labeled              extends Types("labeled")
    case object Unlabeled            extends Types("unlabeled")
    case object Opened               extends Types("opened")
    case object Edited               extends Types("edited")
    case object Closed               extends Types("closed")
    case object Reopened             extends Types("reopened")
    case object Synchronize          extends Types("synchronize")
    case object ReadyForReview       extends Types("ready_for_review")
    case object Locked               extends Types("locked")
    case object Unlocked             extends Types("unlocked")
    case object ReviewRequested      extends Types("review_requested")
    case object ReviewRequestRemoved extends Types("review_request_removed")

  }

}
