package com.alejandrohdezma.github.actions.events

/** Runs your workflow anytime someone forks a repository, which triggers the `fork` event.
  *
  * @see [[https://developer.github.com/v3/repos/forks/#create-a-fork]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#fork-event-fork]]
  */
case object Fork extends Event
