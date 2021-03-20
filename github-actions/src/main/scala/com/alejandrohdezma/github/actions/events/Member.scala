package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the member event occurs. More than one activity type triggers this event.
  *
  * @param types
  *   selects the types of activity that will trigger a workflow run.
  *
  * @see
  *   See more information about the [[https://developer.github.com/v3/repos/collaborators/RESTAPI]].
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#member-event-member]]
  */
final case class Member(types: List[Member.Types]) extends Event {

  def onAdded() = copy(types = types :+ Member.Types.Added)

  def onEdited() = copy(types = types :+ Member.Types.Edited)

  def onDeleted() = copy(types = types :+ Member.Types.Deleted)

}

object Member {

  implicit val MemberEncoder: Encoder[Member] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NonEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Added   extends Types(NonEmptyString.unsafe("added"))
    case object Edited  extends Types(NonEmptyString.unsafe("edited"))
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
