package com.alejandrohdezma.github.actions.base

/** Represents a list of glob patterns that use the * and ** wildcard characters to match more than one branch, tag or
  * path. The ones matched will be ignored in the following events: [[events.PullRequest pull_request]],
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
  * [[dsl.Events.ignore ignore]] on one of the dsl methods available in the different events:
  *
  *   - [[events.PullRequest.branches(ignore* pull_request.branches-ignore]]
  *   - [[events.PullRequest.paths(ignore* pull_request.paths-ignore]]
  *   - [[events.PullRequest.tags(ignore* pull_request.tags-ignore]]
  *   - [[events.PullRequestTarget.branches(ignore* pull_request_target.branches-ignore]]
  *   - [[events.PullRequestTarget.paths(ignore* pull_request_target.paths-ignore]]
  *   - [[events.PullRequestTarget.tags(ignore* pull_request_target.tags-ignore]]
  *   - [[events.Push.branches(ignore* push.branches-ignore]]
  *   - [[events.Push.paths(ignore* push.paths-ignore]]
  *   - [[events.Push.tags(ignore* push.tags-ignore]]
  *   - [[events.WorkflowRun.branches(ignore* workflow_run.branches-ignore]]
  */
final case class Ignoring(value: NonEmptyList[NonEmptyString])
