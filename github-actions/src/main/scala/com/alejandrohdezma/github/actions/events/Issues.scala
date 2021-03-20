package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.NotEmptyString
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

    case object Opened       extends Types("opened")
    case object Edited       extends Types("edited")
    case object Deleted      extends Types("deleted")
    case object Transferred  extends Types("transferred")
    case object Pinned       extends Types("pinned")
    case object Unpinned     extends Types("unpinned")
    case object Closed       extends Types("closed")
    case object Reopened     extends Types("reopened")
    case object Assigned     extends Types("assigned")
    case object Unassigned   extends Types("unassigned")
    case object Labeled      extends Types("labeled")
    case object Unlabeled    extends Types("unlabeled")
    case object Locked       extends Types("locked")
    case object Unlocked     extends Types("unlocked")
    case object Milestoned   extends Types("milestoned")
    case object Demilestoned extends Types("demilestoned")

  }

}
