package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the milestone event occurs. More than one activity type triggers this event.
  *
  * @param types
  *   selects the types of activity that will trigger a workflow run.
  *
  * @see
  *   See more information about the [[https://developer.github.com/v3/issues/milestones/RESTAPI]].
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#milestone-event-milestone]]
  */
final case class Milestone(types: List[Milestone.Types]) extends Event {

  def onCreated() = copy(types = types :+ Milestone.Types.Created)

  def onClosed() = copy(types = types :+ Milestone.Types.Closed)

  def onOpened() = copy(types = types :+ Milestone.Types.Opened)

  def onEdited() = copy(types = types :+ Milestone.Types.Edited)

  def onDeleted() = copy(types = types :+ Milestone.Types.Deleted)

}

object Milestone {

  implicit val MilestoneEncoder: Encoder[Milestone] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NonEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Created extends Types(NonEmptyString.unsafe("created"))
    case object Closed  extends Types(NonEmptyString.unsafe("closed"))
    case object Opened  extends Types(NonEmptyString.unsafe("opened"))
    case object Edited  extends Types(NonEmptyString.unsafe("edited"))
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
