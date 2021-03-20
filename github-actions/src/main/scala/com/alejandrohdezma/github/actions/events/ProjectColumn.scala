package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `project_column` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/projects/columns]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#project-column-event-project_column]]
  */
final case class ProjectColumn(types: List[ProjectColumn.Types]) extends Event {

  /** Launch this workflow when a "created" action is performed on a project column. */
  def onCreated() = copy(types = types :+ ProjectColumn.Types.Created)

  /** Launch this workflow when a "updated" action is performed on a project column. */
  def onUpdated() = copy(types = types :+ ProjectColumn.Types.Updated)

  /** Launch this workflow when a "moved" action is performed on a project column. */
  def onMoved() = copy(types = types :+ ProjectColumn.Types.Moved)

  /** Launch this workflow when a "deleted" action is performed on a project column. */
  def onDeleted() = copy(types = types :+ ProjectColumn.Types.Deleted)

}

/** Contains implicit values and classes relevant to [[ProjectColumn]]. */
object ProjectColumn {

  /** Allows converting a [[ProjectColumn]] value into [[yaml.Yaml yaml]]. */
  implicit val ProjectColumnEncoder: Encoder[ProjectColumn] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[ProjectColumn]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A "created" action is performed on a project column. */
    case object Created extends Types(NonEmptyString.unsafe("created"))

    /** A "updated" action is performed on a project column. */
    case object Updated extends Types(NonEmptyString.unsafe("updated"))

    /** A "moved" action is performed on a project column. */
    case object Moved extends Types(NonEmptyString.unsafe("moved"))

    /** A "deleted" action is performed on a project column. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
