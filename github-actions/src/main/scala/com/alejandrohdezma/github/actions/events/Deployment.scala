package com.alejandrohdezma.github.actions.events

/** Runs your workflow anytime someone creates a deployment, which triggers the deployment event. Deployments created
  * with a commit SHA may not have a Git ref.
  *
  * @see [[https://developer.github.com/v3/repos/deployments/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#deployment-event-deployment]]
  */
case object Deployment extends Event
