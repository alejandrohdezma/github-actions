/*
 * Copyright 2021 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.github.actions

import io.circe.Encoder
import io.circe.Json
import io.circe.syntax._

/**
 * A strategy creates a build matrix for your jobs. You can define different variations of an environment to run each
 * job in.
 *
 * @param matrix a build matrix is a set of different configurations of the virtual environment
 * @param failFast when set to true, GitHub cancels all in-progress jobs if any matrix job fails
 * @param maxParallel the maximum number of jobs that can run simultaneously when using a matrix job strategy. By
 *   default, GitHub will maximize the number of jobs run in parallel depending on the available runners on
 *   GitHub-hosted virtual machines
 *
 * @see [[https://help.github.com/en/actions/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#jobsjob_idstrategy]]
 */
final case class Strategy(matrix: Matrix, failFast: Option[Boolean] = None, maxParallel: Option[Int] = None)

object Strategy {

  implicit val StrategyEncoder: Encoder[Strategy] = strategy =>
    Json.obj(
      "matrix"       := strategy.matrix,
      "fail-fast"    := strategy.failFast,
      "max-parallel" := strategy.maxParallel
    )

}
