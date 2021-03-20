package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * Runs your workflow anytime the project_column event occurs. More than one activity type triggers this event.
 *
 * @param types selects the types of activity that will trigger a workflow run.
 *
 * @see See more information about the [[https://developer.github.com/v3/projects/columns REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#project-column-event-project_column]]
 */
final case class ProjectColumn(types: List[ProjectColumn.Types]) extends Event {

  def onCreated() = copy(types = types :+ ProjectColumn.Types.Created)

  def onUpdated() = copy(types = types :+ ProjectColumn.Types.Updated)

  def onMoved() = copy(types = types :+ ProjectColumn.Types.Moved)

  def onDeleted() = copy(types = types :+ ProjectColumn.Types.Deleted)

}

object ProjectColumn {

  implicit val ProjectColumnEncoder: Encoder[ProjectColumn] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Created extends Types(NotEmptyString.unsafe("created"))
    case object Updated extends Types(NotEmptyString.unsafe("updated"))
    case object Moved   extends Types(NotEmptyString.unsafe("moved"))
    case object Deleted extends Types(NotEmptyString.unsafe("deleted"))

  }

}
