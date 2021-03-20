package com.alejandrohdezma.github.actions.events

/** Runs your workflow anytime someone pushes to a GitHub Pages-enabled branch, which triggers the `page_build` event.
  *
  * @see [[https://developer.github.com/v3/repos/pages/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#page-build-event-page_build]]
  */
case object PageBuild extends Event
