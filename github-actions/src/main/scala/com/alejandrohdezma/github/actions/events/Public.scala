package com.alejandrohdezma.github.actions.events

/** Runs your workflow anytime someone makes a private repository public, which triggers the `public` event.
  *
  * @see [[https://developer.github.com/v3/repos/#edit]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#public-event-public]]
  */
case object Public extends Event
