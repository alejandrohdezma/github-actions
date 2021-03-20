package com.alejandrohdezma.github.actions.events

/** Runs your workflow when someone creates or updates a Wiki page, which triggers the gollum event.
  *
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#gollum-event-gollum]]
  */
case object Gollum extends Event
