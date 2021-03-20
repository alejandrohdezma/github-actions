package com.alejandrohdezma.github.actions.events

/** Runs your workflow anytime a third party provides a deployment status, which triggers the deployment_status event.
  * Deployments created with a commit SHA may not have a Git ref.
  *
  * @see
  *   See more information about the
  *   [[https://developer.github.com/v3/repos/deployments/#create-a-deployment-statusRESTAPI]].
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#deployment-status-event-deployment_status]]
  */
case object DeploymentStatus extends Event
