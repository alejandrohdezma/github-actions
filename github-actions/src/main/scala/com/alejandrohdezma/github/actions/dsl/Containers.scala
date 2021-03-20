package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Container
import com.alejandrohdezma.github.actions.base.NonEmptyString

/** Contains methods for creating a [[Container]], so users don't need to use that class directly. */
trait Containers {

  /** Creates a container from a valid image name. The value can be the Docker Hub image name or a registry name.
    *
    * @example {{{image("ubuntu:latest")}}}
    * @param image The image name.
    * @return A container.
    */
  def image(image: NonEmptyString): Container = Container(image = image)

}
