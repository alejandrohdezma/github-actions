package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `milestone` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/issues/milestones/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#milestone-event-milestone]]
  */
final case class Milestone(types: List[Milestone.Types]) extends Event {

  /** Launch this workflow when a milestone is created. */
  def onCreated() = copy(types = types :+ Milestone.Types.Created)

  /** Launch this workflow when a milestone is closed. */
  def onClosed() = copy(types = types :+ Milestone.Types.Closed)

  /** Launch this workflow when a milestone is opened. */
  def onOpened() = copy(types = types :+ Milestone.Types.Opened)

  /** Launch this workflow when a milestone is edited. */
  def onEdited() = copy(types = types :+ Milestone.Types.Edited)

  /** Launch this workflow when a milestone is deleted. */
  def onDeleted() = copy(types = types :+ Milestone.Types.Deleted)

}

/** Contains implicit values and classes relevant to [[Milestone]]. */
object Milestone {

  /** Allows converting a [[Milestone]] value into [[yaml.Yaml yaml]]. */
  implicit val MilestoneEncoder: Encoder[Milestone] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[Milestone]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A milestone is created. */
    case object Created extends Types(NonEmptyString.unsafe("created"))

    /** A milestone is closed. */
    case object Closed extends Types(NonEmptyString.unsafe("closed"))

    /** A milestone is opened. */
    case object Opened extends Types(NonEmptyString.unsafe("opened"))

    /** A milestone is edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** A milestone is deleted. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
