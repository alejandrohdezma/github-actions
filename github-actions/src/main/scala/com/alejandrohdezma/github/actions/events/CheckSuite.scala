package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * Runs your workflow anytime the check_suite event occurs. More than one activity type triggers this event.
 *
 * @param types selects the types of activity that will trigger a workflow run.
 *
 * @see See more information about the [[https://developer.github.com/v3/checks/suites/ REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#check-suite-event-check_suite]]
 */
final case class CheckSuite(types: List[CheckSuite.Types]) extends Event {

  def onCompleted() = copy(types = types :+ CheckSuite.Types.Completed)

  def onRequested() = copy(types = types :+ CheckSuite.Types.Requested)

  def onRerequested() = copy(types = types :+ CheckSuite.Types.Rerequested)

}

object CheckSuite {

  implicit val CheckSuiteEncoder: Encoder[CheckSuite] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Completed   extends Types(NotEmptyString.unsafe("completed"))
    case object Requested   extends Types(NotEmptyString.unsafe("requested"))
    case object Rerequested extends Types(NotEmptyString.unsafe("rerequested"))

  }

}
