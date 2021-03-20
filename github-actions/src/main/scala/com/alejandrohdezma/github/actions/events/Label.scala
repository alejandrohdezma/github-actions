package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the label event occurs. More than one activity type triggers this event.
  *
  * @param types
  *   selects the types of activity that will trigger a workflow run.
  *
  * @see
  *   See more information about the [[https://developer.github.com/v3/issues/labels/RESTAPI]].
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#label-event-label]]
  */
final case class Label(types: List[Label.Types]) extends Event {

  def onCreated() = copy(types = types :+ Label.Types.Created)

  def onEdited() = copy(types = types :+ Label.Types.Edited)

  def onDeleted() = copy(types = types :+ Label.Types.Deleted)

}

object Label {

  implicit val LabelEncoder: Encoder[Label] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NonEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Created extends Types(NonEmptyString.unsafe("created"))
    case object Edited  extends Types(NonEmptyString.unsafe("edited"))
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
