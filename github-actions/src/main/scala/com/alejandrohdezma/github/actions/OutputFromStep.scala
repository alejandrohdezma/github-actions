package com.alejandrohdezma.github.actions

final case class OutputFromStep(step: NotEmptyString, name: Option[NotEmptyString])
