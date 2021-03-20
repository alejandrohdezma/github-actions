package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.Branches
import com.alejandrohdezma.github.actions.base.Ignore
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.base.NotEmptyList
import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * This event occurs when a workflow run is requested or completed, and allows you to execute a workflow based on the
 * finished result of another workflow. For example, if your pull_request workflow generates build artifacts, you can
 * create a new workflow that uses workflow_run to analyze the results and add a comment to the original pull request.
 *
 * @param types selects the types of activity that will trigger a workflow run.
 *
 * @see [[https://docs.github.com/en/actions/reference/events-that-trigger-workflows#workflow_run]]
 */
final case class WorkflowRun(
    types: List[WorkflowRun.Types],
    workflows: NotEmptyList[NotEmptyString],
    branches: Option[Branches]
) extends Event {

  def onRequested() = copy(types = types :+ WorkflowRun.Types.Requested)

  def onCompleted() = copy(types = types :+ WorkflowRun.Types.Completed)

  def onWorkflow(workflow: NotEmptyString): WorkflowRun = onWorkflows(workflow)

  def onWorkflows(first: NotEmptyString, rest: NotEmptyString*): WorkflowRun =
    copy(workflows = workflows ::: NotEmptyList.of(first, rest: _*))

  def branches(matching: Matching): WorkflowRun = copy(branches = Some(Branches.Matching(matching.value)))

  def branches(ignore: Ignore): WorkflowRun = copy(branches = Some(Branches.Ignore(ignore.value)))

}

object WorkflowRun {

  implicit val WorkflowRunEncoder: Encoder[WorkflowRun] = event =>
    Yaml
      .obj(
        "types"     := event.types,
        "workflows" := event.workflows
      )
      .merge(event.branches.asYaml)

  final case class Builder(types: List[WorkflowRun.Types], branches: Option[Branches]) {

    def types(newTypes: Types*): Builder = copy(types = types ++ newTypes)

    def onWorkflow(workflow: NotEmptyString): WorkflowRun = onWorkflows(workflow)

    def onWorkflows(first: NotEmptyString, rest: NotEmptyString*): WorkflowRun =
      WorkflowRun(types, NotEmptyList.of(first, rest: _*), branches)

    def branches(branches: Branches): Builder = copy(branches = Some(branches))

  }

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Requested extends Types(NotEmptyString.unsafe("requested"))
    case object Completed extends Types(NotEmptyString.unsafe("completed"))

  }

}
