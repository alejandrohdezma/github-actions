package com.alejandrohdezma.github.actions.events

/**
 * Runs your workflow anytime the status of a Git commit changes, which triggers the status event.
 *
 * @see See more information about the [[https://developer.github.com/v3/repos/statuses/ REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#status-event-status]]
 */
case object Status extends Event
