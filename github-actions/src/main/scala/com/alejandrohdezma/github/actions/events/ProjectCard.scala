package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `project_card` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/projects/cards]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#project-card-event-project_card]]
  */
final case class ProjectCard(types: List[ProjectCard.Types]) extends Event {

  /** Launch this workflow when a project's card is created. */
  def onCreated() = copy(types = types :+ ProjectCard.Types.Created)

  /** Launch this workflow when a project's card is moved to another column. */
  def onMoved() = copy(types = types :+ ProjectCard.Types.Moved)

  /** Launch this workflow when a project's card is converted to an issue. */
  def onConverted() = copy(types = types :+ ProjectCard.Types.Converted)

  /** Launch this workflow when a project's card is edited. */
  def onEdited() = copy(types = types :+ ProjectCard.Types.Edited)

  /** Launch this workflow when a project's card is deleted. */
  def onDeleted() = copy(types = types :+ ProjectCard.Types.Deleted)

}

/** Contains implicit values and classes relevant to [[ProjectCard]]. */
object ProjectCard {

  /** Allows converting a [[ProjectCard]] value into [[yaml.Yaml yaml]]. */
  implicit val ProjectCardEncoder: Encoder[ProjectCard] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[ProjectCard]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A project's card is created. */
    case object Created extends Types(NonEmptyString.unsafe("created"))

    /** A project's card is moved to another column. */
    case object Moved extends Types(NonEmptyString.unsafe("moved"))

    /** A project's card is converted to an issue. */
    case object Converted extends Types(NonEmptyString.unsafe("converted"))

    /** A project's card is edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** A project's card is deleted. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
