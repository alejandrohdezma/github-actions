package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Container
import com.alejandrohdezma.github.actions.base.NonEmptyString

trait Containers {

  def image(image: NonEmptyString): Container = Container(image = image)

}
