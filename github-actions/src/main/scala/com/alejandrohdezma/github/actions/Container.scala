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

import cats.data.NonEmptyList
import cats.data.NonEmptyMap

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Encoder
import io.circe.Json
import io.circe.refined._
import io.circe.syntax._

/**
 * A container to run any steps in a job that don't already specify a container. If you have steps that use both script
 * and container actions, the container actions will run as sibling containers on the same network with the same volume
 * mounts.
 *
 * If you do not set a container, all steps will run directly on the host specified by runs-on unless a step refers to
 * an action configured to run in a container
 *
 * @param image the Docker image to use as the container to run the action. The value can be the Docker Hub image name
 *   or a registry name
 * @param credentials if the image's container registry requires authentication to pull the image, you can use
 *   credentials to set a map of the username and password. The credentials are the same values that you would provide
 *   to the `docker login` command
 * @param env sets an array of environment variables in the container
 * @param ports sets an array of ports to expose on the container
 * @param volumes sets an array of volumes for the container to use. You can use volumes to share data between services
 *   or other steps in a job. You can specify named Docker volumes, anonymous Docker volumes, or bind mounts on the
 *   host. To specify a volume, you specify the source and destination path: <source>:<destinationPath>. The <source> is
 *   a volume name or an absolute path on the host machine, and <destinationPath> is an absolute path in the container
 */
final case class Container(
    image: NonEmptyString,
    credentials: Option[Container.Credentials] = None,
    env: Option[NonEmptyMap[NonEmptyString, NonEmptyString]] = None,
    ports: Option[NonEmptyList[PortNumber]] = None,
    volumes: Option[NonEmptyList[String Refined MatchesRegex[W.`"^[^:]+:[^:]+$"`.T]]] = None,
    options: Option[NonEmptyString] = None
)

object Container {

  final case class Credentials(username: NonEmptyString, password: NonEmptyString)

  object Credentials {

    implicit val CredentialsEncoder: Encoder[Credentials] = credentials =>
      Json.obj("username" := credentials.username, "password" := credentials.password)

  }

  implicit val ContainerEncoder: Encoder[Container] = container =>
    Json.obj(
      "image"       := container.image,
      "credentials" := container.credentials,
      "env"         := container.env,
      "ports"       := container.ports,
      "volumes"     := container.volumes,
      "options"     := container.options
    )

}
