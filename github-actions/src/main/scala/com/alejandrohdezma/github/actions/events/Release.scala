package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * Runs your workflow anytime the release event occurs. More than one activity type triggers this event.
 *
 * @param types selects the types of activity that will trigger a workflow run.
 *
 * @see See more information about the [[https://developer.github.com/v3/repos/releases/ in the GitHub Developer documentation REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#release-event-release]]
 */
final case class Release(types: List[Release.Types]) extends Event {

  def onPublished() = copy(types = types :+ Release.Types.Published)

  def onUnpublished() = copy(types = types :+ Release.Types.Unpublished)

  def onCreated() = copy(types = types :+ Release.Types.Created)

  def onEdited() = copy(types = types :+ Release.Types.Edited)

  def onDeleted() = copy(types = types :+ Release.Types.Deleted)

  def onPrereleased() = copy(types = types :+ Release.Types.Prereleased)

  def onReleased() = copy(types = types :+ Release.Types.Released)

}

object Release {

  implicit val ReleaseEncoder: Encoder[Release] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Published   extends Types("published")
    case object Unpublished extends Types("unpublished")
    case object Created     extends Types("created")
    case object Edited      extends Types("edited")
    case object Deleted     extends Types("deleted")
    case object Prereleased extends Types("prereleased")
    case object Released    extends Types("released")

  }

}
