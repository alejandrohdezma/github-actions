package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the issues event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/issues]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#issues-event-issues]]
  */
final case class Issues(types: List[Issues.Types]) extends Event {

  /** Launch this workflow when an issue is opened. */
  def onOpened() = copy(types = types :+ Issues.Types.Opened)

  /** Launch this workflow when an issue is edited. */
  def onEdited() = copy(types = types :+ Issues.Types.Edited)

  /** Launch this workflow when an issue is deleted. */
  def onDeleted() = copy(types = types :+ Issues.Types.Deleted)

  /** Launch this workflow when an issue is transferred to another repository. */
  def onTransferred() = copy(types = types :+ Issues.Types.Transferred)

  /** Launch this workflow when an issue is pinned. */
  def onPinned() = copy(types = types :+ Issues.Types.Pinned)

  /** Launch this workflow when an issue is unpinned. */
  def onUnpinned() = copy(types = types :+ Issues.Types.Unpinned)

  /** Launch this workflow when an issue is closed. */
  def onClosed() = copy(types = types :+ Issues.Types.Closed)

  /** Launch this workflow when an issue is reopened. */
  def onReopened() = copy(types = types :+ Issues.Types.Reopened)

  /** Launch this workflow when an issue is assigned to a user. */
  def onAssigned() = copy(types = types :+ Issues.Types.Assigned)

  /** Launch this workflow when an issue is unassigned from a user. */
  def onUnassigned() = copy(types = types :+ Issues.Types.Unassigned)

  /** Launch this workflow when an issue is labeled. */
  def onLabeled() = copy(types = types :+ Issues.Types.Labeled)

  /** Launch this workflow when an issue is unlabeled. */
  def onUnlabeled() = copy(types = types :+ Issues.Types.Unlabeled)

  /** Launch this workflow when an issue is locked. */
  def onLocked() = copy(types = types :+ Issues.Types.Locked)

  /** Launch this workflow when an issue is unlocked. */
  def onUnlocked() = copy(types = types :+ Issues.Types.Unlocked)

  /** Launch this workflow when an issue is milestoned. */
  def onMilestoned() = copy(types = types :+ Issues.Types.Milestoned)

  /** Launch this workflow when an issue is de-milestoned. */
  def onDemilestoned() = copy(types = types :+ Issues.Types.Demilestoned)

}

/** Contains implicit values and classes relevant to [[Issues]]. */
object Issues {

  /** Allows converting a [[Issues]] value into [[yaml.Yaml yaml]]. */
  implicit val IssuesEncoder: Encoder[Issues] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[Issues]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** An issue is opened. */
    case object Opened extends Types(NonEmptyString.unsafe("opened"))

    /** An issue is edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** An issue is deleted. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

    /** An issue is transferred to another repository. */
    case object Transferred extends Types(NonEmptyString.unsafe("transferred"))

    /** An issue is pinned. */
    case object Pinned extends Types(NonEmptyString.unsafe("pinned"))

    /** An issue is unpinned. */
    case object Unpinned extends Types(NonEmptyString.unsafe("unpinned"))

    /** An issue is closed. */
    case object Closed extends Types(NonEmptyString.unsafe("closed"))

    /** An issue is reopened. */
    case object Reopened extends Types(NonEmptyString.unsafe("reopened"))

    /** An issue is assigned to a user. */
    case object Assigned extends Types(NonEmptyString.unsafe("assigned"))

    /** An issue is unassigned from a user. */
    case object Unassigned extends Types(NonEmptyString.unsafe("unassigned"))

    /** An issue is labeled. */
    case object Labeled extends Types(NonEmptyString.unsafe("labeled"))

    /** An issue is unlabeled. */
    case object Unlabeled extends Types(NonEmptyString.unsafe("unlabeled"))

    /** An issue is locked. */
    case object Locked extends Types(NonEmptyString.unsafe("locked"))

    /** An issue is unlocked. */
    case object Unlocked extends Types(NonEmptyString.unsafe("unlocked"))

    /** An issue is milestoned. */
    case object Milestoned extends Types(NonEmptyString.unsafe("milestoned"))

    /** An issue is de-milestoned. */
    case object Demilestoned extends Types(NonEmptyString.unsafe("demilestoned"))

  }

}
