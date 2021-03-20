package com.alejandrohdezma.github.actions.events

/**
 * Runs your workflow anytime when someone forks a repository, which triggers the fork event.
 *
 * @see See more information about the [[https://developer.github.com/v3/repos/forks/#create-a-fork REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#fork-event-fork]]
 */
case object Fork extends Event
