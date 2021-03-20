package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `check_run` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/checks/runs]]
  * @see [[https://docs.github.com/en/developers/webhooks-and-events/webhook-events-and-payloads#check_run]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#check-run-event-check_run]]
  */
final case class CheckRun(types: List[CheckRun.Types]) extends Event {

  /** Launch this workflow when a new check run was created. */
  def onCreated() = copy(types = types :+ CheckRun.Types.Created)

  /** Launch this workflow when someone requested to re-run your check run from the pull request UI. */
  def onRerequested() = copy(types = types :+ CheckRun.Types.Rerequested)

  /** Launch this workflow when the `status` of the check run is `completed`. */
  def onCompleted() = copy(types = types :+ CheckRun.Types.Completed)

  /** Launch this workflow when someone requested an action your app provides to be taken. */
  def onRequestedAction() = copy(types = types :+ CheckRun.Types.RequestedAction)

}

/** Contains implicit values and classes relevant to [[CheckRun]]. */
object CheckRun {

  /** Allows converting a [[CheckRun]] value into [[yaml.Yaml yaml]]. */
  implicit val CheckRunEncoder: Encoder[CheckRun] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[CheckRun]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A new check run was created. */
    case object Created extends Types(NonEmptyString.unsafe("created"))

    /** Someone requested to re-run your check run from the pull request UI.
      *
      * @see [[https://docs.github.com/en/articles/about-status-checks#checks]]
      */
    case object Rerequested extends Types(NonEmptyString.unsafe("rerequested"))

    /** The `status` of the check run is `completed`. */
    case object Completed extends Types(NonEmptyString.unsafe("completed"))

    /** Someone requested an action your app provides to be taken.
      *
      * [[https://docs.github.com/en/rest/reference/checks#check-runs-and-requested-actions]]
      */
    case object RequestedAction extends Types(NonEmptyString.unsafe("requested_action"))

  }

}
