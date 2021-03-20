package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `release` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/repos/releases/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#release-event-release]]
  */
final case class Release(types: List[Release.Types]) extends Event {

  /** Launch this workflow when a release, pre-release, or draft of a release is published. */
  def onPublished() = copy(types = types :+ Release.Types.Published)

  /** Launch this workflow when a release or pre-release is deleted. */
  def onUnpublished() = copy(types = types :+ Release.Types.Unpublished)

  /** Launch this workflow when a draft is saved, or a release or pre-release is published without previously being
    * saved as a draft.
    */
  def onCreated() = copy(types = types :+ Release.Types.Created)

  /** Launch this workflow when a release, pre-release, or draft release is edited. */
  def onEdited() = copy(types = types :+ Release.Types.Edited)

  /** Launch this workflow when a release, pre-release, or draft release is deleted. */
  def onDeleted() = copy(types = types :+ Release.Types.Deleted)

  /** Launch this workflow when a pre-release is created. */
  def onPrereleased() = copy(types = types :+ Release.Types.Prereleased)

  /** Launch this workflow when a release or draft of a release is published, or a pre-release is changed to a
    * release.
    */
  def onReleased() = copy(types = types :+ Release.Types.Released)

}

/** Contains implicit values and classes relevant to [[Release]]. */
object Release {

  /** Allows converting a [[Release]] value into [[yaml.Yaml yaml]]. */
  implicit val ReleaseEncoder: Encoder[Release] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[Release]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A release, pre-release, or draft of a release is published. */
    case object Published extends Types(NonEmptyString.unsafe("published"))

    /** A release or pre-release is deleted. */
    case object Unpublished extends Types(NonEmptyString.unsafe("unpublished"))

    /** A draft is saved, or a release or pre-release is published without previously being saved as a draft. */
    case object Created extends Types(NonEmptyString.unsafe("created"))

    /** A release, pre-release, or draft release is edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** A release, pre-release, or draft release is deleted. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

    /** A pre-release is created. */
    case object Prereleased extends Types(NonEmptyString.unsafe("prereleased"))

    /** A release or draft of a release is published, or a pre-release is changed to a release. */
    case object Released extends Types(NonEmptyString.unsafe("released"))

  }

}
