package com.alejandrohdezma.github.actions.events

/** Runs your workflow anytime the `watch` event occurs. More than one activity type triggers this event.
  *
  * @see [[https://developer.github.com/v3/activity/starring/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#watch-event-watch]]
  */
case object Watch extends Event
