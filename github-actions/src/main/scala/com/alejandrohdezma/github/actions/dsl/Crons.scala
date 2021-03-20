package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Cron
import com.alejandrohdezma.github.actions.Hour

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Crons {

  /**
   * Cron that runs the workflow at 00:00 on January 1st every year.
   */
  lazy val yearly: Cron = "0 0 1 1 *"

  /**
   * Cron that runs the workflow at 00:00 on the first day of the month.
   */
  lazy val monthly: Cron = "0 0 1 * *"

  /**
   * Cron that runs the workflow at 00:00 every Sunday.
   */
  lazy val weekly: Cron = "0 0 * * 0"

  /**
   * Cron that runs the workflow at 00:00 everyday.
   */
  lazy val daily: Cron = "0 0 * * *"

  /**
   * Cron that runs the workflow at minute 0 of every hour.
   */
  lazy val hourly: Cron = "0 * * * *"

  /**
   * Cron that runs the workflow everyday at the provided hour.
   */
  def everydayAt(hour: Hour): Cron = Cron.unsafe(s"0 ${hour.value} * * *")

}
