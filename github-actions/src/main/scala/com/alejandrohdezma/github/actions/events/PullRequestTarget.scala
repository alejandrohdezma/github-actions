package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.Ignoring
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** This event is similar to pull_request, except that it runs in the context of the base repository of the pull
  * request, rather than in the merge commit. This means that you can more safely make your secrets available to the
  * workflows triggered by the pull request, because only workflows defined in the commit on the base repository are
  * run. For example, this event allows you to create workflows that label and comment on pull requests, based on the
  * contents of the event payload.
  *
  * @param types
  *   selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://docs.github.com/en/actions/reference/events-that-trigger-workflows#pull_request_target]]
  */
final case class PullRequestTarget(
    types: List[PullRequestTarget.Types],
    branches: Option[Either[Ignoring, Matching]] = None,
    tags: Option[Either[Ignoring, Matching]] = None,
    paths: Option[Either[Ignoring, Matching]] = None
) extends Event {

  def onAssigned() = copy(types = types :+ PullRequestTarget.Types.Assigned)

  def onUnassigned() = copy(types = types :+ PullRequestTarget.Types.Unassigned)

  def onLabeled() = copy(types = types :+ PullRequestTarget.Types.Labeled)

  def onUnlabeled() = copy(types = types :+ PullRequestTarget.Types.Unlabeled)

  def onOpened() = copy(types = types :+ PullRequestTarget.Types.Opened)

  def onEdited() = copy(types = types :+ PullRequestTarget.Types.Edited)

  def onClosed() = copy(types = types :+ PullRequestTarget.Types.Closed)

  def onReopened() = copy(types = types :+ PullRequestTarget.Types.Reopened)

  def onSynchronize() =
    copy(types = types :+ PullRequestTarget.Types.Synchronize)

  def onReadyForReview() =
    copy(types = types :+ PullRequestTarget.Types.ReadyForReview)

  def onLocked() = copy(types = types :+ PullRequestTarget.Types.Locked)

  def onUnlocked() = copy(types = types :+ PullRequestTarget.Types.Unlocked)

  def onReviewRequested() =
    copy(types = types :+ PullRequestTarget.Types.ReviewRequested)

  def onReviewRequestRemoved() =
    copy(types = types :+ PullRequestTarget.Types.ReviewRequestRemoved)

  def branches(matching: Matching): PullRequestTarget =
    copy(branches = Some(Right(matching)))

  def branches(ignore: Ignoring): PullRequestTarget =
    copy(branches = Some(Left(ignore)))

  def tags(matching: Matching): PullRequestTarget =
    copy(tags = Some(Right(matching)))

  def tags(ignore: Ignoring): PullRequestTarget =
    copy(tags = Some(Left(ignore)))

  def paths(matching: Matching): PullRequestTarget =
    copy(paths = Some(Right(matching)))

  def paths(ignore: Ignoring): PullRequestTarget =
    copy(paths = Some(Left(ignore)))

}

object PullRequestTarget {

  implicit val PullRequestTargetEncoder: Encoder[PullRequestTarget] = event =>
    Yaml.obj(
      "types"           := event.types,
      "branches"        := event.branches.flatMap(_.toOption).map(_.value),
      "branches-ignore" := event.branches.flatMap(_.swap.toOption).map(_.value),
      "tags"            := event.tags.flatMap(_.toOption).map(_.value),
      "tags-ignore"     := event.tags.flatMap(_.swap.toOption).map(_.value),
      "paths"           := event.paths.flatMap(_.toOption).map(_.value),
      "paths-ignore"    := event.paths.flatMap(_.swap.toOption).map(_.value)
    )

  sealed abstract class Types(val value: NonEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Assigned             extends Types(NonEmptyString.unsafe("assigned"))
    case object Unassigned           extends Types(NonEmptyString.unsafe("unassigned"))
    case object Labeled              extends Types(NonEmptyString.unsafe("labeled"))
    case object Unlabeled            extends Types(NonEmptyString.unsafe("unlabeled"))
    case object Opened               extends Types(NonEmptyString.unsafe("opened"))
    case object Edited               extends Types(NonEmptyString.unsafe("edited"))
    case object Closed               extends Types(NonEmptyString.unsafe("closed"))
    case object Reopened             extends Types(NonEmptyString.unsafe("reopened"))
    case object Synchronize          extends Types(NonEmptyString.unsafe("synchronize"))
    case object ReadyForReview       extends Types(NonEmptyString.unsafe("ready_for_review"))
    case object Locked               extends Types(NonEmptyString.unsafe("locked"))
    case object Unlocked             extends Types(NonEmptyString.unsafe("unlocked"))
    case object ReviewRequested      extends Types(NonEmptyString.unsafe("review_requested"))
    case object ReviewRequestRemoved extends Types(NonEmptyString.unsafe("review_request_removed"))

  }

}
