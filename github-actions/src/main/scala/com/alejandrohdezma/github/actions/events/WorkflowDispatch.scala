package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.Input
import com.alejandrohdezma.github.actions.yaml._

/**
 * You can now create workflows that are manually triggered with the new workflow_dispatch event. You will then see a
 * 'Run workflow' button on the Actions tab, enabling you to easily trigger a run.
 *
 * @param inputs input parameters allow you to specify data that the action expects to use during runtime. GitHub
 *   stores input parameters as environment variables. Input ids with uppercase letters are converted to lowercase
 *   during runtime. We recommended using lowercase input ids.
 *
 * @see [[https://github.blog/changelog/2020-07-06-github-actions-manual-triggers-with-workflow_dispatch/]]
 */
final case class WorkflowDispatch(inputs: List[Input]) extends Event {

  def inputs(newInputs: Input*): WorkflowDispatch = copy(inputs = inputs ++ newInputs)

}

object WorkflowDispatch {

  implicit val WorkflowDispatchEncoder: Encoder[WorkflowDispatch] = _.inputs.map(_.asYaml).reduce((a, b) => b.merge(a))

}
