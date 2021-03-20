package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.Branches
import com.alejandrohdezma.github.actions.Paths
import com.alejandrohdezma.github.actions.Tags
import com.alejandrohdezma.github.actions.base.Ignore
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * Runs your workflow anytime the pull_request event occurs. More than one activity type triggers this event.
 *
 * Note: Workflows do not run on private base repositories when you open a pull request from a forked repository.
 *
 * When you create a pull request from a forked repository to the base repository, GitHub sends the pull_request
 * event to the base repository and no pull request events occur on the forked repository.
 *
 * Workflows don't run on forked repositories by default. You must enable GitHub Actions in the Actions tab of the
 * forked repository.
 *
 * The permissions for the GITHUB_TOKEN in forked repositories is read-only. For more information about the
 * GITHUB_TOKEN, see https://help.github.com/en/articles/virtual-environments-for-github-actions.
 *
 * @param types selects the types of activity that will trigger a workflow run.
 *
 * @see See more information about the [[https://developer.github.com/v3/pulls REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#pull-request-event-pull_request]]
 */
final case class PullRequest(
    types: List[PullRequest.Types],
    branches: Option[Branches] = None,
    tags: Option[Tags] = None,
    paths: Option[Paths] = None
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

  def onReadyForReview() = copy(types = types :+ PullRequest.Types.ReadyForReview)

  def onLocked() = copy(types = types :+ PullRequest.Types.Locked)

  def onUnlocked() = copy(types = types :+ PullRequest.Types.Unlocked)

  def onReviewRequested() = copy(types = types :+ PullRequest.Types.ReviewRequested)

  def onReviewRequestRemoved() = copy(types = types :+ PullRequest.Types.ReviewRequestRemoved)

  def types(extraTypes: PullRequest.Types*): PullRequest = copy(types = types ++ extraTypes)

  def branches(matching: Matching): PullRequest = copy(branches = Some(Branches.Matching(matching.value)))
  def branches(ignore: Ignore): PullRequest     = copy(branches = Some(Branches.Ignore(ignore.value)))

  def tags(matching: Matching): PullRequest = copy(tags = Some(Tags.Matching(matching.value)))
  def tags(ignore: Ignore): PullRequest     = copy(tags = Some(Tags.Ignore(ignore.value)))

  def paths(matching: Matching): PullRequest = copy(paths = Some(Paths.Matching(matching.value)))
  def paths(ignore: Ignore): PullRequest     = copy(paths = Some(Paths.Ignore(ignore.value)))

}

object PullRequest {

  implicit val PullRequestEncoder: Encoder[PullRequest] = event =>
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
