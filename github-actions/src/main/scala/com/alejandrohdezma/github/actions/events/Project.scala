package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `project` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/projects/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#project-event-project]]
  */
final case class Project(types: List[Project.Types]) extends Event {

  /** Launch this workflow when a project is created. */
  def onCreated() = copy(types = types :+ Project.Types.Created)

  /** Launch this workflow when a project is updated. */
  def onUpdated() = copy(types = types :+ Project.Types.Updated)

  /** Launch this workflow when a project is closed. */
  def onClosed() = copy(types = types :+ Project.Types.Closed)

  /** Launch this workflow when a project is reopened. */
  def onReopened() = copy(types = types :+ Project.Types.Reopened)

  /** Launch this workflow when a project is edited. */
  def onEdited() = copy(types = types :+ Project.Types.Edited)

  /** Launch this workflow when a project is deleted. */
  def onDeleted() = copy(types = types :+ Project.Types.Deleted)

}

/** Contains implicit values and classes relevant to [[Project]]. */
object Project {

  /** Allows converting a [[Project]] value into [[yaml.Yaml yaml]]. */
  implicit val ProjectEncoder: Encoder[Project] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[Project]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A project is created. */
    case object Created extends Types(NonEmptyString.unsafe("created"))

    /** A project is updated. */
    case object Updated extends Types(NonEmptyString.unsafe("updated"))

    /** A project is closed. */
    case object Closed extends Types(NonEmptyString.unsafe("closed"))

    /** A project is reopened. */
    case object Reopened extends Types(NonEmptyString.unsafe("reopened"))

    /** A project is edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** A project is deleted. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
