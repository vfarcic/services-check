package com.technologyconversations.services.check

import java.io.File
import java.nio.file.{Paths, Files}

import com.typesafe.config._
import org.specs2.mutable.Specification
import scala.collection.mutable.ArrayBuffer
import scalaj.http._
import scala.util.Properties._
import collection.JavaConversions._
import CheckConfig._

class CheckSpec extends Specification {

  val conf = ConfigFactory.parseFile(new File("application.conf"))
  val domain = envOrElse("DOMAIN", conf.getString("check.domain"))
  val connTimeoutMs = envOrElse("CONN_TIMEOUT_MS", conf.getString("check.connTimeoutMs")).toInt
  val readTimeoutMs = envOrElse("READ_TIMEOUT_MS", conf.getString("check.readTimeoutMs")).toInt

  s"$domain" should {

    for (addressConf <- conf.getConfigList("check.addresses").toList) {
      val address = addressConf.getString("address")
      val uri = s"$domain$address"
      val expectedResponseCode = addressConf.getOptionalInt("responseCode").getOrElse(200)
      s"return 200 when $address is requested" in {
        val response: HttpResponse[String] = Http(uri).timeout(connTimeoutMs, readTimeoutMs).asString
        response.code must equalTo(expectedResponseCode)
      }
    }

  }

}
