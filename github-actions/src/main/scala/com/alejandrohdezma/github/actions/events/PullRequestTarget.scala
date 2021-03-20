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
  * Note: By default, a workflow only runs when a pull request's activity type is opened, synchronize, or reopened. To
  * trigger workflows for more activity types, use the `on*` DSL methods.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  * @param branches List of glob patterns that should be used to either match or ignore branches.
  * @param paths List of glob patterns that should be used to either match or ignore paths.
  * @param tags List of glob patterns that should be used to either match or ignore tags.
  *
  * @see [[https://docs.github.com/en/actions/reference/events-that-trigger-workflows#pull_request_target]]
  */
final case class PullRequestTarget(
    types: List[PullRequestTarget.Types],
    branches: Option[Either[Ignoring, Matching]] = None,
    tags: Option[Either[Ignoring, Matching]] = None,
    paths: Option[Either[Ignoring, Matching]] = None
) extends Event {

  /** Launch this workflow when a pull request is assigned. */
  def onAssigned() = copy(types = types :+ PullRequestTarget.Types.Assigned)

  /** Launch this workflow when a pull request is unassigned. */
  def onUnassigned() = copy(types = types :+ PullRequestTarget.Types.Unassigned)

  /** Launch this workflow when a pull request is labeled. */
  def onLabeled() = copy(types = types :+ PullRequestTarget.Types.Labeled)

  /** Launch this workflow when a pull request is unlabeled. */
  def onUnlabeled() = copy(types = types :+ PullRequestTarget.Types.Unlabeled)

  /** Launch this workflow when a pull request is opened. */
  def onOpened() = copy(types = types :+ PullRequestTarget.Types.Opened)

  /** Launch this workflow when a pull request is edited. */
  def onEdited() = copy(types = types :+ PullRequestTarget.Types.Edited)

  /** Launch this workflow when a pull request is closed. */
  def onClosed() = copy(types = types :+ PullRequestTarget.Types.Closed)

  /** Launch this workflow when a pull request is reopened. */
  def onReopened() = copy(types = types :+ PullRequestTarget.Types.Reopened)

  /** Launch this workflow when a pull request is synchronize. */
  def onSynchronize() = copy(types = types :+ PullRequestTarget.Types.Synchronize)

  /** Launch this workflow when a pull request is ready for review. */
  def onReadyForReview() = copy(types = types :+ PullRequestTarget.Types.ReadyForReview)

  /** Launch this workflow when a pull request is locked. */
  def onLocked() = copy(types = types :+ PullRequestTarget.Types.Locked)

  /** Launch this workflow when a pull request is unlocked. */
  def onUnlocked() = copy(types = types :+ PullRequestTarget.Types.Unlocked)

  /** Launch this workflow when a pull request is requested for review. */
  def onReviewRequested() = copy(types = types :+ PullRequestTarget.Types.ReviewRequested)

  /** Launch this workflow when a pull request's review is removed. */
  def onReviewRequestRemoved() = copy(types = types :+ PullRequestTarget.Types.ReviewRequestRemoved)

  /** Specifies the current workflow to run only if the current branch matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[branches(ignore* branches(ignore)]] this will override it.
    *
    * @example {{{pullRequestTarget.branches(matching("releases&#47;**"))}}}
    */
  def branches(matching: Matching): PullRequestTarget = copy(branches = Some(Right(matching)))

  /** Specifies the current workflow to run only if the current branch does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[branches(matching* branches(matching)]] this will override it.
    *
    * @example {{{pullRequestTarget.branches(ignoring("wip&#47;**"))}}}
    */
  def branches(ignore: Ignoring): PullRequestTarget = copy(branches = Some(Left(ignore)))

  /** Specifies the current workflow to run only if the current tag matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[tags(ignore* tags(ignore)]] this will override it.
    *
    * @example {{{pullRequestTarget.tags(matching("v*"))}}}
    */
  def tags(matching: Matching): PullRequestTarget = copy(tags = Some(Right(matching)))

  /** Specifies the current workflow to run only if the current tag does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[tags(matching* tags(matching))]] this will override it.
    *
    * @example {{{pullRequestTarget.tags(ignoring("*-SNAPSHOT"))}}}
    */
  def tags(ignore: Ignoring): PullRequestTarget = copy(tags = Some(Left(ignore)))

  /** Specifies the current workflow to run only if at least one modified file matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[paths(ignore* tags(ignore))]] this will override it.
    *
    * @example {{{pullRequestTarget.paths(matching("**.sbt"))}}}
    */
  def paths(matching: Matching): PullRequestTarget = copy(paths = Some(Right(matching)))

  /** Specifies the current workflow to run only if at least one modified file does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[paths(matching* tags(matching))]] this will override it.
    *
    * @example {{{pullRequestTarget.paths(ignoring("docs&#47;**"))}}}
    */
  def paths(ignore: Ignoring): PullRequestTarget = copy(paths = Some(Left(ignore)))

}

/** Contains implicit values and classes relevant to [[PullRequestTarget]]. */
object PullRequestTarget {

  /** Allows converting a [[PullRequestTarget]] value into [[yaml.Yaml yaml]]. */
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

  /** The different types on which the [[PullRequestTarget]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A pull request is assigned. */
    case object Assigned extends Types(NonEmptyString.unsafe("a`ssigned"))

    /** A pull request is unassigned. */
    case object Unassigned extends Types(NonEmptyString.unsafe("unassigned"))

    /** A pull request is labeled. */
    case object Labeled extends Types(NonEmptyString.unsafe("labeled"))

    /** A pull request is unlabeled. */
    case object Unlabeled extends Types(NonEmptyString.unsafe("unlabeled"))

    /** A pull request is opened. */
    case object Opened extends Types(NonEmptyString.unsafe("opened"))

    /** A pull request is edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** A pull request is closed. */
    case object Closed extends Types(NonEmptyString.unsafe("closed"))

    /** A pull request is reopened. */
    case object Reopened extends Types(NonEmptyString.unsafe("reopened"))

    /** A pull request is synchronize. */
    case object Synchronize extends Types(NonEmptyString.unsafe("synchronize"))

    /** A pull request is ready for review. */
    case object ReadyForReview extends Types(NonEmptyString.unsafe("ready_for_review"))

    /** A pull request is locked. */
    case object Locked extends Types(NonEmptyString.unsafe("locked"))

    /** A pull request is unlocked. */
    case object Unlocked extends Types(NonEmptyString.unsafe("unlocked"))

    /** A pull request is requested for review. */
    case object ReviewRequested extends Types(NonEmptyString.unsafe("review_requested"))

    /** A pull request's review is removed. */
    case object ReviewRequestRemoved extends Types(NonEmptyString.unsafe("review_request_removed"))

  }

}
