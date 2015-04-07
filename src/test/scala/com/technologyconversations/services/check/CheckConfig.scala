package com.technologyconversations.services.check

import com.typesafe.config.Config

object CheckConfig {

  implicit class RichConfig(val underlying: Config) extends AnyVal {

    def getOptionalString(path: String): Option[String] = if (underlying.hasPath(path)) {
      Some(underlying.getString(path))
    } else {
      None
    }

    def getOptionalInt(path: String): Option[Integer] = if (underlying.hasPath(path)) {
      Some(underlying.getInt(path))
    } else {
      None
    }

  }

}
