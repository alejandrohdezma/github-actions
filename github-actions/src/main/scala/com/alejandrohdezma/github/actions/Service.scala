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
 * Additional containers to host services for a job in a workflow. These are useful for creating databases or cache
 * services like redis. The runner on the virtual machine will automatically create a network and manage the life cycle
 * of the service containers.
 *
 * When you use a service container for a job or your step uses container actions, you don't need to set port
 * information to access the service. Docker automatically exposes all ports between containers on the same network.
 *
 * When both the job and the action run in a container, you can directly reference the container by its hostname. The
 * hostname is automatically mapped to the service name.
 *
 * When a step does not use a container action, you must access the service using localhost and bind the ports.
 *
 * @param name the name of the service
 * @param container the container that runs this service
 */
final case class Service(name: String, container: Container)

object Service {

  implicit val ServiceEncoder: Encoder[Service] = service => Json.obj(service.name := service.container)

}
