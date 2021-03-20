package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.Ignoring
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.base.NonEmptyList
import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** This event occurs when a workflow run is requested or completed, and allows you to execute a workflow based on the
  * finished result of another workflow. For example, if your pull_request workflow generates build artifacts, you can
  * create a new workflow that uses workflow_run to analyze the results and add a comment to the original pull request.
  *
  * @param types
  *   selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://docs.github.com/en/actions/reference/events-that-trigger-workflows#workflow_run]]
  */
final case class WorkflowRun(
    types: List[WorkflowRun.Types],
    workflows: NonEmptyList[NonEmptyString],
    branches: Option[Either[Ignoring, Matching]]
) extends Event {

  def onRequested() = copy(types = types :+ WorkflowRun.Types.Requested)

  def onCompleted() = copy(types = types :+ WorkflowRun.Types.Completed)

  def onWorkflow(workflow: NonEmptyString): WorkflowRun = onWorkflows(workflow)

  def onWorkflows(first: NonEmptyString, rest: NonEmptyString*): WorkflowRun =
    copy(workflows = workflows ::: NonEmptyList.of(first, rest: _*))

  def branches(matching: Matching): WorkflowRun =
    copy(branches = Some(Right(matching)))

  def branches(ignore: Ignoring): WorkflowRun =
    copy(branches = Some(Left(ignore)))

}

object WorkflowRun {

  implicit val WorkflowRunEncoder: Encoder[WorkflowRun] = event =>
    Yaml.obj(
      "types"           := event.types,
      "workflows"       := event.workflows,
      "branches"        := event.branches.flatMap(_.toOption).map(_.value),
      "branches-ignore" := event.branches.flatMap(_.swap.toOption).map(_.value)
    )

  final case class Builder(
      types: List[WorkflowRun.Types],
      branches: Option[Either[Ignoring, Matching]]
  ) {

    def types(newTypes: Types*): Builder = copy(types = types ++ newTypes)

    def onWorkflow(workflow: NonEmptyString): WorkflowRun =
      onWorkflows(workflow)

    def onWorkflows(first: NonEmptyString, rest: NonEmptyString*): WorkflowRun =
      WorkflowRun(types, NonEmptyList.of(first, rest: _*), branches)

    def branches(matching: Matching): Builder =
      copy(branches = Some(Right(matching)))

    def branches(ignore: Ignoring): Builder =
      copy(branches = Some(Left(ignore)))

  }

  sealed abstract class Types(val value: NonEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Requested extends Types(NonEmptyString.unsafe("requested"))
    case object Completed extends Types(NonEmptyString.unsafe("completed"))

  }

}
