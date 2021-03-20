package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the project_card event occurs. More than one activity type triggers this event.
  *
  * @param types
  *   selects the types of activity that will trigger a workflow run.
  *
  * @see
  *   See more information about the [[https://developer.github.com/v3/projects/cardsRESTAPI]].
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#project-card-event-project_card]]
  */
final case class ProjectCard(types: List[ProjectCard.Types]) extends Event {

  def onCreated() = copy(types = types :+ ProjectCard.Types.Created)

  def onMoved() = copy(types = types :+ ProjectCard.Types.Moved)

  def onConverted() = copy(types = types :+ ProjectCard.Types.Converted)

  def onEdited() = copy(types = types :+ ProjectCard.Types.Edited)

  def onDeleted() = copy(types = types :+ ProjectCard.Types.Deleted)

}

object ProjectCard {

  implicit val ProjectCardEncoder: Encoder[ProjectCard] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NonEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Created   extends Types(NonEmptyString.unsafe("created"))
    case object Moved     extends Types(NonEmptyString.unsafe("moved"))
    case object Converted extends Types(NonEmptyString.unsafe("converted"))
    case object Edited    extends Types(NonEmptyString.unsafe("edited"))
    case object Deleted   extends Types(NonEmptyString.unsafe("deleted"))

  }

}
