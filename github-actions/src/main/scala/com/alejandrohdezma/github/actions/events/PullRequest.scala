package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.Ignoring
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the pull_request event occurs. More than one activity type triggers this event.
  *
  * Note: Workflows do not run on private base repositories when you open a pull request from a forked repository.
  *
  * When you create a pull request from a forked repository to the base repository, GitHub sends the pull_request event
  * to the base repository and no pull request events occur on the forked repository.
  *
  * Workflows don't run on forked repositories by default. You must enable GitHub Actions in the Actions tab of the
  * forked repository.
  *
  * The permissions for the GITHUB_TOKEN in forked repositories is read-only. For more information about the
  * GITHUB_TOKEN, see https://help.github.com/en/articles/virtual-environments-for-github-actions.
  *
  * @param types
  *   selects the types of activity that will trigger a workflow run.
  * @param branches
  *   The Matching and Ignore accept glob patterns that use the * and ** wildcard characters to match more than one
  *   branch.
  *
  * These patterns are evaluated against the Git ref's name. For example, defining the pattern `mona/octocat` in
  * Matching will match the `refs/heads/mona/octocat` Git ref. The pattern `releases*&#47;**` will match the
  * `refs/heads/releases/10` Git ref.
  *
  * You can use two types of filters to prevent a workflow from running` on pushes and pull requests to branches:
  * Matching or Ignore. You cannot use both filters for the same event in a workflow. Use the Matching filter when you
  * need to filter branches for positive matches and exclude branches. Use the Ignore filter when you only need to
  * exclude branch names.
  *
  * You can exclude branches using the ! character. The order that you define patterns matters.
  *
  *   - A matching negative pattern (prefixed with !) after a positive match will exclude the Git ref.
  *   - A matching positive pattern after a negative match will include the Git ref again.
  *
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#filter-pattern-cheat-sheet]]
  * @param paths
  *   When using the push and pull_request events, you can configure a workflow to run when at least one file does not
  *   match `Paths.Ignore` or at least one modified file matches the configured paths. Path filters are not evaluated
  *   for pushes to tags.
  *
  * The `Paths.Ignore` and `Paths.Matching` keywords accept glob patterns that use the * and ** wildcard characters to
  * match more than one path name.
  *
  * You can exclude paths using two types of filters. You cannot use both of these filters for the same event in a
  * workflow.
  *
  *   - `Paths.Ignore` - Use the `Paths.Ignore` filter when you only need to exclude path names.
  *   - `Paths.Matching` - Use the `Paths.Matching` filter when you need to filter paths for positive matches and
  *     exclude paths.
  *
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#filter-pattern-cheat-sheet]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#onpushpull_requestpaths]]
  * @param tags
  *   The `Tags.Matching` and `Tags.Ignore accept glob patterns that use the * and ** wildcard characters to match more
  *   than one tag.
  *
  * The patterns defined in tags are evaluated against the Git ref's name. For example, defining the pattern `latest` in
  * `Tags.Matching` will match just the `latest` tag. The pattern "v*" will match any tag starting with `v`.
  *
  * You can use two types of filters to prevent a workflow from running on pushes and pull requests to tags:
  *
  *   - `Tags.Matching` or `Tags.Ignore` - You cannot use both the `Tags` and `Tags.Ignore` filters for the same event
  *     in a workflow. Use the `Tags` filter when you need to filter tags for positive matches and exclude tags. Use the
  *     `Tags.Ignore` filter when you only need to exclude tag names.
  *
  * You can exclude tags using the ! character. The order that you define patterns matters.
  *
  *   - A matching negative pattern (prefixed with !) after a positive match will exclude the Git ref.
  *   - A matching positive pattern after a negative match will include the Git ref again.
  *
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#filter-pattern-cheat-sheet]]
  *
  * @see
  *   See more information about the [[https://developer.github.com/v3/pullsRESTAPI]].
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#pull-request-event-pull_request]]
  */
final case class PullRequest(
    types: List[PullRequest.Types],
    branches: Option[Either[Ignoring, Matching]] = None,
    tags: Option[Either[Ignoring, Matching]] = None,
    paths: Option[Either[Ignoring, Matching]] = None
) extends Event {

  def onAssigned() = copy(types = types :+ PullRequest.Types.Assigned)

  def onUnassigned() = copy(types = types :+ PullRequest.Types.Unassigned)

  def onLabeled() = copy(types = types :+ PullRequest.Types.Labeled)

  def onUnlabeled() = copy(types = types :+ PullRequest.Types.Unlabeled)

  def onOpened() = copy(types = types :+ PullRequest.Types.Opened)

  def onEdited() = copy(types = types :+ PullRequest.Types.Edited)

  def onClosed() = copy(types = types :+ PullRequest.Types.Closed)

  def onReopened() = copy(types = types :+ PullRequest.Types.Reopened)

  def onSynchronize() = copy(types = types :+ PullRequest.Types.Synchronize)

  def onReadyForReview() =
    copy(types = types :+ PullRequest.Types.ReadyForReview)

  def onLocked() = copy(types = types :+ PullRequest.Types.Locked)

  def onUnlocked() = copy(types = types :+ PullRequest.Types.Unlocked)

  def onReviewRequested() =
    copy(types = types :+ PullRequest.Types.ReviewRequested)

  def onReviewRequestRemoved() =
    copy(types = types :+ PullRequest.Types.ReviewRequestRemoved)

  def types(extraTypes: PullRequest.Types*): PullRequest =
    copy(types = types ++ extraTypes)

  def branches(matching: Matching): PullRequest =
    copy(branches = Some(Right(matching)))

  def branches(ignore: Ignoring): PullRequest =
    copy(branches = Some(Left(ignore)))

  def tags(matching: Matching): PullRequest = copy(tags = Some(Right(matching)))
  def tags(ignore: Ignoring): PullRequest   = copy(tags = Some(Left(ignore)))

  def paths(matching: Matching): PullRequest =
    copy(paths = Some(Right(matching)))
  def paths(ignore: Ignoring): PullRequest = copy(paths = Some(Left(ignore)))

}

object PullRequest {

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
