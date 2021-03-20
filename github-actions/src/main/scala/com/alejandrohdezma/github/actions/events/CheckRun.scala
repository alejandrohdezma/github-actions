package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the check_run event occurs. More than one activity type triggers this event.
  *
  * @param types
  *   selects the types of activity that will trigger a workflow run.
  *
  * @see
  *   See more information about the [[https://developer.github.com/v3/checks/runsRESTAPI]].
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#check-run-event-check_run]]
  */
final case class CheckRun(types: List[CheckRun.Types]) extends Event {

  def onCreated() = copy(types = types :+ CheckRun.Types.Created)

  def onRerequested() = copy(types = types :+ CheckRun.Types.Rerequested)

  def onCompleted() = copy(types = types :+ CheckRun.Types.Completed)

  def onRequestedAction() =
    copy(types = types :+ CheckRun.Types.RequestedAction)

}

object CheckRun {

  implicit val CheckRunEncoder: Encoder[CheckRun] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NonEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Created         extends Types(NonEmptyString.unsafe("created"))
    case object Rerequested     extends Types(NonEmptyString.unsafe("rerequested"))
    case object Completed       extends Types(NonEmptyString.unsafe("completed"))
    case object RequestedAction extends Types(NonEmptyString.unsafe("requested_action"))

  }

}
