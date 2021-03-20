package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime a package is published or updated.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://help.github.com/en/github/managing-packages-with-github-packages]]
  * @see [[https://help.github.com/en/actions/reference/events-that-trigger-workflows#registry-package-event-registry_package]]
  */
final case class RegistryPackage(types: List[RegistryPackage.Types]) extends Event {

  /** Launch this workflow when a package is published. */
  def onPublished() = copy(types = types :+ RegistryPackage.Types.Published)

  /** Launch this workflow when a package is updated. */
  def onUpdated() = copy(types = types :+ RegistryPackage.Types.Updated)

}

/** Contains implicit values and classes relevant to [[RegistryPackage]]. */
object RegistryPackage {

  /** Allows converting a [[RegistryPackage]] value into [[yaml.Yaml yaml]]. */
  implicit val RegistryPackageEncoder: Encoder[RegistryPackage] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[RegistryPackage]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** A package is published. */
    case object Published extends Types(NonEmptyString.unsafe("published"))

    /** A package is updated. */
    case object Updated extends Types(NonEmptyString.unsafe("updated"))

  }

}
