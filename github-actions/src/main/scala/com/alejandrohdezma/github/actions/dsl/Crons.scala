package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.base.Cron
import com.alejandrohdezma.github.actions.base.Hour

/** Contains typical [[base.Cron cron]] values to be used when running a workflow on a [[schedule]] event. */
@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Crons {

  /** Cron that runs the workflow at 00:00 on January 1st every year. */
  lazy val yearly: Cron = Cron.unsafe("0 0 1 1 *")

  /** Cron that runs the workflow at 00:00 on the first day of the month. */
  lazy val monthly: Cron = Cron.unsafe("0 0 1 * *")

  /** Cron that runs the workflow at 00:00 every Sunday. */
  lazy val weekly: Cron = Cron.unsafe("0 0 * * 0")

  /** Cron that runs the workflow at 00:00 everyday. */
  lazy val daily: Cron = Cron.unsafe("0 0 * * *")

  /** Cron that runs the workflow at minute 0 of every hour. */
  lazy val hourly: Cron = Cron.unsafe("0 * * * *")

  /** Cron that runs the workflow everyday at the provided hour.
    *
    * @param hour The hour at which the workflow should run.
    * @example {{{schedule(everydayAt(5))}}}
    */
  def everydayAt(hour: Hour): Cron = Cron.unsafe(s"0 ${hour.value} * * *")

}
