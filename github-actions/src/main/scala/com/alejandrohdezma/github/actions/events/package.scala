package com.alejandrohdezma.github.actions

/** Contains all the events for which a workflow can react. Most of the time you won't use this classes
  * directly, but instead you will use their default constructors, aliases and their DSLs.
  *
  * Available events are:
  *
  *   - [[CheckRun]] ([[actions.checkRun alias]] / [[actions$.checkRun:* default constructor]]).
  *   - [[CheckSuite]] ([[checkSuite alias]] / [[actions$.checkSuite:* default constructor]]).
  *   - [[Create]] ([[create alias]] / [[actions$.create:* default constructor]]).
  *   - [[Delete]] ([[delete alias]] / [[actions$.delete:* default constructor]]).
  *   - [[Deployment]] ([[deployment alias]] / [[actions$.deployment:* default constructor]]).
  *   - [[DeploymentStatus]] ([[deploymentStatus alias]] / [[actions$.deploymentStatus:* default constructor]]).
  *   - [[Fork]] ([[fork alias]] / [[actions$.fork:* default constructor]]).
  *   - [[Gollum]] ([[gollum alias]] / [[actions$.gollum:* default constructor]]).
  *   - [[IssueComment]] ([[issueComment alias]] / [[actions$.issueComment:* default constructor]]).
  *   - [[Issues]] ([[issues alias]] / [[actions$.issues:* default constructor]]).
  *   - [[Label]] ([[label alias]] / [[actions$.label:* default constructor]]).
  *   - [[Member]] ([[member alias]] / [[actions$.member:* default constructor]]).
  *   - [[Milestone]] ([[milestone alias]] / [[actions$.milestone:* default constructor]]).
  *   - [[PageBuild]] ([[pageBuild alias]] / [[actions$.pageBuild:* default constructor]]).
  *   - [[Project]] ([[project alias]] / [[actions$.project:* default constructor]]).
  *   - [[ProjectCard]] ([[projectCard alias]] / [[actions$.projectCard:* default constructor]]).
  *   - [[ProjectColumn]] ([[projectColumn alias]] / [[actions$.projectColumn:* default constructor]]).
  *   - [[Public]] ([[public alias]] / [[actions$.public:* default constructor]]).
  *   - [[PullRequest]] ([[pullRequest alias]] / [[actions$.pullRequest:* default constructor]]).
  *   - [[PullRequestReview]] ([[pullRequestReview alias]] / [[actions$.pullRequestReview:* default constructor]]).
  *   - [[PullRequestReviewComment]] ([[pullRequestReviewComment alias]] / [[actions$.pullRequestReviewComment:* default constructor]]).
  *   - [[PullRequestTarget]] ([[pullRequestTarget alias]] / [[actions$.pullRequestTarget:* default constructor]]).
  *   - [[Push]] ([[push alias]] / [[actions$.push:* default constructor]]).
  *   - [[RegistryPackage]] ([[registryPackage alias]] / [[actions$.registryPackage:* default constructor]]).
  *   - [[Release]] ([[release alias]] / [[actions$.release:* default constructor]]).
  *   - [[Status]] ([[status alias]] / [[actions$.status:* default constructor]]).
  *   - [[Watch]] ([[watch alias]] / [[actions$.watch:* default constructor]]).
  *   - [[WorkflowDispatch]] ([[workflowDispatch alias]] / [[actions$.workflowDispatch:* default constructor]]).
  *   - [[WorkflowRun]] ([[workflowRun alias]] / [[actions$.workflowRun:* default constructor]]).
  *   - [[RepositoryDispatch]] ([[repositoryDispatch alias]] / [[actions$.repositoryDispatch:* default constructor]]).
  *   - [[Schedule]] ([[schedule alias]] / [[actions$.schedule(* default constructor]]).
  *
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows]]
  */
package object events
