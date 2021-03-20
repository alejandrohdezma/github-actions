package com.alejandrohdezma.github.actions.events

/** Runs your workflow anytime the status of a Git commit changes, which triggers the `status` event.
  *
  * @see [[https://developer.github.com/v3/repos/statuses/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#status-event-status]]
  */
case object Status extends Event
