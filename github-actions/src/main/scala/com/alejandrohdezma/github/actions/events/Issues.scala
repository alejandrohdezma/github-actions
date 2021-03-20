package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * Runs your workflow anytime the issues event occurs. More than one activity type triggers this event.
 *
 * @param types selects the types of activity that will trigger a workflow run.
 *
 * @see See more information about the [[https://developer.github.com/v3/issues REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#issues-event-issues]]
 */
final case class Issues(types: List[Issues.Types]) extends Event {

  def onOpened() = copy(types = types :+ Issues.Types.Opened)

  def onEdited() = copy(types = types :+ Issues.Types.Edited)

  def onDeleted() = copy(types = types :+ Issues.Types.Deleted)

  def onTransferred() = copy(types = types :+ Issues.Types.Transferred)

  def onPinned() = copy(types = types :+ Issues.Types.Pinned)

  def onUnpinned() = copy(types = types :+ Issues.Types.Unpinned)

  def onClosed() = copy(types = types :+ Issues.Types.Closed)

  def onReopened() = copy(types = types :+ Issues.Types.Reopened)

  def onAssigned() = copy(types = types :+ Issues.Types.Assigned)

  def onUnassigned() = copy(types = types :+ Issues.Types.Unassigned)

  def onLabeled() = copy(types = types :+ Issues.Types.Labeled)

  def onUnlabeled() = copy(types = types :+ Issues.Types.Unlabeled)

  def onLocked() = copy(types = types :+ Issues.Types.Locked)

  def onUnlocked() = copy(types = types :+ Issues.Types.Unlocked)

  def onMilestoned() = copy(types = types :+ Issues.Types.Milestoned)

  def onDemilestoned() = copy(types = types :+ Issues.Types.Demilestoned)

}

object Issues {

  implicit val IssuesEncoder: Encoder[Issues] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Opened       extends Types(NotEmptyString.unsafe("opened"))
    case object Edited       extends Types(NotEmptyString.unsafe("edited"))
    case object Deleted      extends Types(NotEmptyString.unsafe("deleted"))
    case object Transferred  extends Types(NotEmptyString.unsafe("transferred"))
    case object Pinned       extends Types(NotEmptyString.unsafe("pinned"))
    case object Unpinned     extends Types(NotEmptyString.unsafe("unpinned"))
    case object Closed       extends Types(NotEmptyString.unsafe("closed"))
    case object Reopened     extends Types(NotEmptyString.unsafe("reopened"))
    case object Assigned     extends Types(NotEmptyString.unsafe("assigned"))
    case object Unassigned   extends Types(NotEmptyString.unsafe("unassigned"))
    case object Labeled      extends Types(NotEmptyString.unsafe("labeled"))
    case object Unlabeled    extends Types(NotEmptyString.unsafe("unlabeled"))
    case object Locked       extends Types(NotEmptyString.unsafe("locked"))
    case object Unlocked     extends Types(NotEmptyString.unsafe("unlocked"))
    case object Milestoned   extends Types(NotEmptyString.unsafe("milestoned"))
    case object Demilestoned extends Types(NotEmptyString.unsafe("demilestoned"))

  }

}
