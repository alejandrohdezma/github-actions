package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `label` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/issues/labels/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#label-event-label]]
  */
final case class Label(types: List[Label.Types]) extends Event {

  /** Launch this workflow when a label is created. */
  def onCreated() = copy(types = types :+ Label.Types.Created)

  /** Launch this workflow when a label is edited. */
  def onEdited() = copy(types = types :+ Label.Types.Edited)

  /** Launch this workflow when a label is deleted. */
  def onDeleted() = copy(types = types :+ Label.Types.Deleted)

}

/** Contains implicit values and classes relevant to [[Label]]. */
object Label {

  /** Allows converting a [[Label]] value into [[yaml.Yaml yaml]]. */
  implicit val LabelEncoder: Encoder[Label] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[Label]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A label is created. */
    case object Created extends Types(NonEmptyString.unsafe("created"))

    /** A label is edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** A label is deleted. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
