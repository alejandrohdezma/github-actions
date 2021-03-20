package com.alejandrohdezma.github.actions.events

/** Runs your workflow anytime someone creates a branch or tag, which triggers the create event.
  *
  * @see [[https://developer.github.com/v3/git/refs/#create-a-reference]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#create-event-create]]
  */
case object Create extends Event
