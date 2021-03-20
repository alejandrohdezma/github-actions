package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** You can use the GitHub API to trigger a webhook event called `repository_dispatch` when you want to trigger a
  * workflow for activity that happens outside of GitHub.
  *
  * To trigger the custom `repository_dispatch` webhook event, you must send a POST request to a GitHub API endpoint and
  * provide an `event_type` name to describe the activity type. To trigger a workflow run, you must also configure your
  * workflow to use the `repository_dispatch` event.
  *
  * @param types The `event_type` under which the `repository_dispatch` should trigger this workflow.
  *
  * @see [[https://developer.github.com/v3/repos/#create-a-repository-dispatch-event]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#external-events-repository_dispatch]]
  */
final case class RepositoryDispatch(types: List[NonEmptyString]) extends Event {

  /** Add new `event_type` under which the `repository_dispatch` should trigger this workflow.
    *
    * @example {{{repositoryDispatch.types("on-demand-test")}}}
    */
  def types(newTypes: NonEmptyString*): RepositoryDispatch = copy(types = types ++ newTypes)

}

/** Contains implicit values relevant to [[RepositoryDispatch]]. */
object RepositoryDispatch {

  /** Allows converting a [[RepositoryDispatch]] value into [[yaml.Yaml yaml]]. */
  implicit val RepositoryDispatchEncoder: Encoder[RepositoryDispatch] = event => Yaml.obj("types" := event.types)

}
