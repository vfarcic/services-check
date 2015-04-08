package com.technologyconversations.services.check

import java.io.File

import com.typesafe.config._
import org.specs2.matcher.JsonMatchers
import org.specs2.mutable.Specification
import scala.util.parsing.json.{JSONFormat, JSONType, JSON, JSONObject}
import scalaj.http._
import scala.util.Properties._
import collection.JavaConversions._
import CheckConfig._

class CheckSpec extends Specification with JsonMatchers {

  val conf = ConfigFactory.parseFile(new File("application.conf"))
  val domain = envOrElse("DOMAIN", conf.getString("check.domain"))
  val connTimeoutMs = envOrElse("CONN_TIMEOUT_MS", conf.getString("check.connTimeoutMs")).toInt
  val readTimeoutMs = envOrElse("READ_TIMEOUT_MS", conf.getString("check.readTimeoutMs")).toInt

  s"$domain" should {

    for (addressConf <- conf.getConfigList("check.apis").toList) {
      val address = addressConf.getString("address")
      val uri = s"$domain$address"
      val expectedResponseCode = addressConf.getOptionalInt("responseCode").getOrElse(200)
      val method = addressConf.getOptionalString("method").getOrElse("GET")
      val response: HttpResponse[String] = Http(uri)
        .method(method)
        .timeout(connTimeoutMs, readTimeoutMs)
        .asString
      s"return 200 when $address is requested" in {
        response.code must equalTo(expectedResponseCode)
      }
      for ((key, value) <- getResponseBodyMap(addressConf, "responseBodyRoot")) {
        s"have ($key -> $value) in the root when $address is requested" in {
          response.body must */(key -> value)
        }
      }
      for ((key, value) <- getResponseBodyMap(addressConf, "responseBodyAnywhere")) {
        s"have ($key -> $value) anywhere when $address is requested" in {
          response.body must */(key -> value)
        }
      }
    }

  }

  def getResponseBodyMap(config: Config, path: String): Map[String, String] = {
    val bodyConfig = config.getOptionalConfig(path)
    if (bodyConfig.isEmpty) {
      Map()
    } else {
      (for (responseBodyEntry <- bodyConfig.get.entrySet()) yield
      (responseBodyEntry.getKey, bodyConfig.get.getString(responseBodyEntry.getKey))
        ).toMap
    }
  }

}
