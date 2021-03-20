package com.alejandrohdezma.github.actions.events

/**
 * Runs your workflow anytime someone deletes a branch or tag, which triggers the delete event.
 *
 * @see See more information about the [[https://developer.github.com/v3/git/refs/#delete-a-reference REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#delete-event-delete]]
 */
case object Delete extends Event
