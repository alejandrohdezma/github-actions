package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.Ignoring
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.base.NonEmptyList
import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** This event occurs when a workflow run is requested or completed, and allows you to execute a workflow based on the
  * finished result of another workflow. For example, if your `pull_request` workflow generates build artifacts, you can
  * create a new workflow that uses `workflow_run` to analyze the results and add a comment to the original pull request.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  * @param workflows The list of workflows to what this event should react.
  * @param branches List of glob patterns that should be used to either match or ignore branches.
  *
  * @see [[https://docs.github.com/en/actions/reference/events-that-trigger-workflows#workflow_run]]
  */
final case class WorkflowRun(
    types: List[WorkflowRun.Types],
    workflows: NonEmptyList[NonEmptyString],
    branches: Option[Either[Ignoring, Matching]]
) extends Event {

  /** Launch this workflow when a run for one of the selected workflows is requested. */
  def onRequested() = copy(types = types :+ WorkflowRun.Types.Requested)

  /** Launch this workflow when a run for one of the selected workflows is completed. */
  def onCompleted() = copy(types = types :+ WorkflowRun.Types.Completed)

  /** Add a new workflow to the list of watched workflows. */
  def onWorkflow(workflow: NonEmptyString): WorkflowRun = onWorkflows(workflow)

  /** Add a bunch of workflows to the list of watched workflows. */
  def onWorkflows(first: NonEmptyString, rest: NonEmptyString*): WorkflowRun =
    copy(workflows = workflows ::: NonEmptyList.of(first, rest: _*))

  /** Specifies the current workflow to run only if the current branch matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[branches(ignore* branches(ignore)]] this will override it.
    *
    * @example {{{pullRequestTarget.branches(matching("releases&#47;**"))}}}
    */
  def branches(matching: Matching): WorkflowRun = copy(branches = Some(Right(matching)))

  /** Specifies the current workflow to run only if the current branch does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[branches(matching* branches(matching)]] this will override it.
    *
    * @example {{{pullRequestTarget.branches(ignoring("wip&#47;**"))}}}
    */
  def branches(ignore: Ignoring): WorkflowRun = copy(branches = Some(Left(ignore)))

}

/** Contains implicit values and classes relevant to [[WorkflowRun]]. */
object WorkflowRun {

  /** Allows converting a [[WorkflowRun]] value into [[yaml.Yaml yaml]]. */
  implicit val WorkflowRunEncoder: Encoder[WorkflowRun] = event =>
    Yaml.obj(
      "types"           := event.types,
      "workflows"       := event.workflows,
      "branches"        := event.branches.flatMap(_.toOption).map(_.value),
      "branches-ignore" := event.branches.flatMap(_.swap.toOption).map(_.value)
    )

  /** Builder for the [[WorkflowRun]] class. Once either [[Builder.onWorkflow onWorkflow]] or
    * [[Builder.onWorkflows onWorkflows]] are called a [[WorkflowRun]] value is returned (which also
    * gives access to the same DSL methods as this builder).
    *
    * @param types Selects the types of activity that will trigger a workflow run.
    * @param branches List of glob patterns that should be used to either match or ignore branches.
    */
  final case class Builder(types: List[WorkflowRun.Types], branches: Option[Either[Ignoring, Matching]]) {

    /** Launch this workflow when a run for one of the selected workflows is requested. */
    def onRequested() = copy(types = types :+ WorkflowRun.Types.Requested)

    /** Launch this workflow when a run for one of the selected workflows is completed. */
    def onCompleted() = copy(types = types :+ WorkflowRun.Types.Completed)

    /** Add a new workflow to the list of watched workflows. */
    def onWorkflow(workflow: NonEmptyString): WorkflowRun = onWorkflows(workflow)

    /** Add a bunch of workflows to the list of watched workflows. */
    def onWorkflows(first: NonEmptyString, rest: NonEmptyString*): WorkflowRun =
      WorkflowRun(types, NonEmptyList.of(first, rest: _*), branches)

    /** Specifies the current workflow to run only if the current branch matches the provided patterns.
      *
      * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
      * call to [[branches(ignore* branches(ignore)]] this will override it.
      *
      * @example {{{workflowRun.branches(matching("releases&#47;**"))}}}
      */
    def branches(matching: Matching): Builder = copy(branches = Some(Right(matching)))

    /** Specifies the current workflow to run only if the current branch does not match the provided patterns.
      *
      * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
      * call to [[branches(matching* branches(matching)]] this will override it.
      *
      * @example {{{workflowRun.branches(ignoring("wip&#47;**"))}}}
      */
    def branches(ignore: Ignoring): Builder = copy(branches = Some(Left(ignore)))

  }

  /** The different types on which the [[WorkflowRun]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A run for one of the selected workflows is requested. */
    case object Requested extends Types(NonEmptyString.unsafe("requested"))

    /** A run for one of the selected workflows is completed. */
    case object Completed extends Types(NonEmptyString.unsafe("completed"))

  }

}
