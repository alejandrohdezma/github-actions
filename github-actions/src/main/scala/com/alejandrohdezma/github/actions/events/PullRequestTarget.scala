package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.Branches
import com.alejandrohdezma.github.actions.Paths
import com.alejandrohdezma.github.actions.Tags
import com.alejandrohdezma.github.actions.base.NotEmptyString
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

    case object Assigned             extends Types(NotEmptyString.unsafe("assigned"))
    case object Unassigned           extends Types(NotEmptyString.unsafe("unassigned"))
    case object Labeled              extends Types(NotEmptyString.unsafe("labeled"))
    case object Unlabeled            extends Types(NotEmptyString.unsafe("unlabeled"))
    case object Opened               extends Types(NotEmptyString.unsafe("opened"))
    case object Edited               extends Types(NotEmptyString.unsafe("edited"))
    case object Closed               extends Types(NotEmptyString.unsafe("closed"))
    case object Reopened             extends Types(NotEmptyString.unsafe("reopened"))
    case object Synchronize          extends Types(NotEmptyString.unsafe("synchronize"))
    case object ReadyForReview       extends Types(NotEmptyString.unsafe("ready_for_review"))
    case object Locked               extends Types(NotEmptyString.unsafe("locked"))
    case object Unlocked             extends Types(NotEmptyString.unsafe("unlocked"))
    case object ReviewRequested      extends Types(NotEmptyString.unsafe("review_requested"))
    case object ReviewRequestRemoved extends Types(NotEmptyString.unsafe("review_request_removed"))

  }

}
