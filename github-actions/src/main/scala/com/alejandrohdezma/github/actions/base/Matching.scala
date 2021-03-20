package com.alejandrohdezma.github.actions.base

/** Represents a list of glob patterns that use the * and ** wildcard characters to match more than one branch, tag or
  * path. The ones matched will be filtered in the following events: [[events.PullRequest pull_request]],
  * [[events.PullRequestTarget pull_request_target]], [[events.Push push]] and [[events.WorkflowRun workflow_run]].
  *
  * ==Overview==
  * These patterns are evaluated against the Git ref's name (in the case of branches or tags) or path
  * name (in the case of paths). For example, defining the pattern `mona/octocat` for branches will match the
  * `refs/heads/mona/octocat` Git ref. The pattern `releases*&#47;**` will match the `refs/heads/releases/10` Git ref.
  * While in the case of tags the pattern "v*" will match any tag starting with `v`.
  *
  * You cannot use both the [[Matching]] and [[Ignoring]] filters for the same event category in a workflow. Use
  * [[Matching]] when you need to filter tags, branches or paths for positive matches and/or exclude. Use [[Ignoring]]
  * when you only need to exclude tags, branches or paths.
  *
  * ==Usage==
  * Usually you will never need to use this class directly. Instead you will DSL's smart constructor
  * [[dsl.Events.matching matching]] on one of the dsl methods available in the different events:
  *
  *   - [[events.PullRequest.branches(matching* pull_request.branches]]
  *   - [[events.PullRequest.paths(matching* pull_request.paths]]
  *   - [[events.PullRequest.tags(matching* pull_request.tags]]
  *   - [[events.PullRequestTarget.branches(matching* pull_request_target.branches]]
  *   - [[events.PullRequestTarget.paths(matching* pull_request_target.paths]]
  *   - [[events.PullRequestTarget.tags(matching* pull_request_target.tags]]
  *   - [[events.Push.branches(matching* push.branches]]
  *   - [[events.Push.paths(matching* push.paths]]
  *   - [[events.Push.tags(matching* push.tags]]
  *   - [[events.WorkflowRun.branches(matching* workflow_run.branches]]
  */
final case class Matching(value: NonEmptyList[NonEmptyString])
