package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `check_suite` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/checks/suites/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#check-suite-event-check_suite]]
  */
final case class CheckSuite(types: List[CheckSuite.Types]) extends Event {

  /** Launch this workflow when all check runs in a check suite have completed. */
  def onCompleted() = copy(types = types :+ CheckSuite.Types.Completed)

  /** Launch this workflow when new code is pushed to the app's repository. */
  def onRequested() = copy(types = types :+ CheckSuite.Types.Requested)

  /** Launch this workflow when someone requests to re-run the entire check suite from the pull request UI. */
  def onRerequested() = copy(types = types :+ CheckSuite.Types.Rerequested)

}

/** Contains implicit values and classes relevant to [[CheckSuite]]. */
object CheckSuite {

  /** Allows converting a [[CheckSuite]] value into [[yaml.Yaml yaml]]. */
  implicit val CheckSuiteEncoder: Encoder[CheckSuite] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[CheckSuite]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** All check runs in a check suite have completed. */
    case object Completed extends Types(NonEmptyString.unsafe("completed"))

    /** Occurs when new code is pushed to the app's repository. */
    case object Requested extends Types(NonEmptyString.unsafe("requested"))

    /** Occurs when someone requests to re-run the entire check suite from the pull request UI. */
    case object Rerequested extends Types(NonEmptyString.unsafe("rerequested"))

  }

}
