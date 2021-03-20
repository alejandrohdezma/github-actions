package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.Cron
import com.alejandrohdezma.github.actions.base.NonEmptyList
import com.alejandrohdezma.github.actions.yaml._

/** You can schedule a workflow to run at specific UTC times using POSIX
  * [[https://pubs.opengroup.org/onlinepubs/9699919799/utilities/crontab.html#tag_20_25_07 cron syntax]].
  *
  * Scheduled workflows run on the latest commit on the default or base branch. The shortest interval you can run
  * scheduled workflows is once every 5 minutes.
  *
  * You can use [[https://crontab.guru/ crontab guru]] to help generate your cron syntax and confirm what time it will
  * run. To help you get started, there is also a list of [[https://crontab.guru/examples.html crontab guru examples]].
  *
  * @param crons The list of cron patterns when the workflow should run.
  *
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#scheduled-events-schedule]]
  */
final case class Schedule(crons: NonEmptyList[Cron]) extends Event

/** Contains implicit values relevant to [[Schedule]]. */
object Schedule {

  /** Allows converting a [[Schedule]] value into [[yaml.Yaml yaml]]. */
  implicit val ScheduleEncoder: Encoder[Schedule] =
    _.crons.map(cron => Yaml.obj("cron" := cron)).asYaml

}
