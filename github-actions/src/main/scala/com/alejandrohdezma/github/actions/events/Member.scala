package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `member` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/repos/collaborators/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#member-event-member]]
  */
final case class Member(types: List[Member.Types]) extends Event {

  /** Launch this workflow when a user accepts an invitation to a repository. */
  def onAdded() = copy(types = types :+ Member.Types.Added)

  /** Launch this workflow when a user is removed as a collaborator in a repository. */
  def onEdited() = copy(types = types :+ Member.Types.Edited)

  /** Launch this workflow when a user's collaborator permissions have changed. */
  def onDeleted() = copy(types = types :+ Member.Types.Deleted)

}

/** Contains implicit values and classes relevant to [[Member]]. */
object Member {

  /** Allows converting a [[Member]] value into [[yaml.Yaml yaml]]. */
  implicit val MemberEncoder: Encoder[Member] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[Member]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A user accepts an invitation to a repository. */
    case object Added extends Types(NonEmptyString.unsafe("added"))

    /** A user is removed as a collaborator in a repository. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** A user's collaborator permissions have changed. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
