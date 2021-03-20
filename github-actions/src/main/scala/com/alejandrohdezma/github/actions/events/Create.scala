package com.alejandrohdezma.github.actions.events

/** Runs your workflow anytime someone creates a branch or tag, which triggers the create event.
  *
  * @see
  *   See more information about the [[https://developer.github.com/v3/git/refs/#create-a-referenceRESTAPI]].
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#create-event-create]]
  */
case object Create extends Event
