package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * Runs your workflow anytime a package is published or updated.
 *
 * @see [[https://help.github.com/en/github/managing-packages-with-github-packages]]
 *
 * @param types selects the types of activity that will trigger a workflow run.
 *
 * @see [[https://help.github.com/en/actions/reference/events-that-trigger-workflows#registry-package-event-registry_package]]
 */
final case class RegistryPackage(types: List[RegistryPackage.Types]) extends Event {

  def onPublished() = copy(types = types :+ RegistryPackage.Types.Published)

  def onUpdated() = copy(types = types :+ RegistryPackage.Types.Updated)

}

object RegistryPackage {

  implicit val RegistryPackageEncoder: Encoder[RegistryPackage] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Published extends Types("published")
    case object Updated   extends Types("updated")

  }

}
