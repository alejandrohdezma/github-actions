package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.Ignoring
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `pull_request` event occurs. More than one activity type triggers this event.
  *
  * Note: By default, a workflow only runs when a pull request's activity type is opened, synchronize, or reopened. To
  * trigger workflows for more activity types, use the `on*` DSL methods.
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
  * @param branches List of glob patterns that should be used to either match or ignore branches.
  * @param paths List of glob patterns that should be used to either match or ignore paths.
  * @param tags List of glob patterns that should be used to either match or ignore tags.
  *
  * @see [[https://developer.github.com/v3/pulls]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#pull-request-event-pull_request]]
  */
final case class PullRequest(
    types: List[PullRequest.Types],
    branches: Option[Either[Ignoring, Matching]] = None,
    tags: Option[Either[Ignoring, Matching]] = None,
    paths: Option[Either[Ignoring, Matching]] = None
) extends Event {

  /** Launch this workflow when a pull request is assigned. */
  def onAssigned() = copy(types = types :+ PullRequest.Types.Assigned)

  /** Launch this workflow when a pull request is unassigned. */
  def onUnassigned() = copy(types = types :+ PullRequest.Types.Unassigned)

  /** Launch this workflow when a pull request is labeled. */
  def onLabeled() = copy(types = types :+ PullRequest.Types.Labeled)

  /** Launch this workflow when a pull request is unlabeled. */
  def onUnlabeled() = copy(types = types :+ PullRequest.Types.Unlabeled)

  /** Launch this workflow when a pull request is opened. */
  def onOpened() = copy(types = types :+ PullRequest.Types.Opened)

  /** Launch this workflow when a pull request is edited. */
  def onEdited() = copy(types = types :+ PullRequest.Types.Edited)

  /** Launch this workflow when a pull request is closed. */
  def onClosed() = copy(types = types :+ PullRequest.Types.Closed)

  /** Launch this workflow when a pull request is reopened. */
  def onReopened() = copy(types = types :+ PullRequest.Types.Reopened)

  /** Launch this workflow when a pull request is synchronize. */
  def onSynchronize() = copy(types = types :+ PullRequest.Types.Synchronize)

  /** Launch this workflow when a pull request is ready for review. */
  def onReadyForReview() = copy(types = types :+ PullRequest.Types.ReadyForReview)

  /** Launch this workflow when a pull request is locked. */
  def onLocked() = copy(types = types :+ PullRequest.Types.Locked)

  /** Launch this workflow when a pull request is unlocked. */
  def onUnlocked() = copy(types = types :+ PullRequest.Types.Unlocked)

  /** Launch this workflow when a pull request is requested for review. */
  def onReviewRequested() = copy(types = types :+ PullRequest.Types.ReviewRequested)

  /** Launch this workflow when a pull request's review is removed. */
  def onReviewRequestRemoved() = copy(types = types :+ PullRequest.Types.ReviewRequestRemoved)

  /** Specifies the current workflow to run only if the current branch matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[branches(ignore* branches(ignore)]] this will override it.
    *
    * @example {{{pullRequest.branches(matching("releases&#47;**"))}}}
    */
  def branches(matching: Matching): PullRequest = copy(branches = Some(Right(matching)))

  /** Specifies the current workflow to run only if the current branch does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[branches(matching* branches(matching)]] this will override it.
    *
    * @example {{{pullRequest.branches(ignoring("wip&#47;**"))}}}
    */
  def branches(ignore: Ignoring): PullRequest = copy(branches = Some(Left(ignore)))

  /** Specifies the current workflow to run only if the current tag matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[tags(ignore* tags(ignore)]] this will override it.
    *
    * @example {{{pullRequest.tags(matching("v*"))}}}
    */
  def tags(matching: Matching): PullRequest = copy(tags = Some(Right(matching)))

  /** Specifies the current workflow to run only if the current tag does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[tags(matching* tags(matching))]] this will override it.
    *
    * @example {{{pullRequest.tags(ignoring("*-SNAPSHOT"))}}}
    */
  def tags(ignore: Ignoring): PullRequest = copy(tags = Some(Left(ignore)))

  /** Specifies the current workflow to run only if at least one modified file matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[paths(ignore* tags(ignore))]] this will override it.
    *
    * @example {{{pullRequest.paths(matching("**.sbt"))}}}
    */
  def paths(matching: Matching): PullRequest = copy(paths = Some(Right(matching)))

  /** Specifies the current workflow to run only if at least one modified file does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[paths(matching* tags(matching))]] this will override it.
    *
    * @example {{{pullRequest.paths(ignoring("docs&#47;**"))}}}
    */
  def paths(ignore: Ignoring): PullRequest = copy(paths = Some(Left(ignore)))

}

/** Contains implicit values and classes relevant to [[PullRequest]]. */
object PullRequest {

  /** Allows converting a [[PullRequest]] value into [[yaml.Yaml yaml]]. */
  implicit val PullRequestEncoder: Encoder[PullRequest] = event =>
    Yaml.obj(
      "types"           := event.types,
      "branches"        := event.branches.flatMap(_.toOption).map(_.value),
      "branches-ignore" := event.branches.flatMap(_.swap.toOption).map(_.value),
      "tags"            := event.tags.flatMap(_.toOption).map(_.value),
      "tags-ignore"     := event.tags.flatMap(_.swap.toOption).map(_.value),
      "paths"           := event.paths.flatMap(_.toOption).map(_.value),
      "paths-ignore"    := event.paths.flatMap(_.swap.toOption).map(_.value)
    )

  /** The different types on which the [[PullRequest]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A pull request is assigned. */
    case object Assigned extends Types(NonEmptyString.unsafe("assigned"))

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
