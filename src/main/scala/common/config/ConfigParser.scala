package net.ingtra.sparking
package common.config

import io.circe.yaml.parser
import sttp.client3._

import scala.io.Source

class ConfigParser(private val urlString: String) {
  private val protocol = urlString.split("://")(0)
  private val path = urlString.split("://")(1)
  private val yamlString = protocol match {
    case "http" => readHttp(urlString)
    case "https" => readHttp(urlString)
    case "file" => readFile("/" + path)
  }

  def getAs[T](implicit d: io.circe.Decoder[T]): T = {
    val json = parser.parse(yamlString) match {
      case Left(err) => throw new InvalidConfigException(err)
      case Right(value) => value
    }

    json.as[T] match {
      case Left(err) => throw new InvalidConfigException(err)
      case Right(value) => value
    }
  }

  private def readHttp(url: String): String = {
    val backend = HttpURLConnectionBackend()
    val response = basicRequest.get(uri"$url").response(asString.getRight).send(backend)
    response.body
  }

  private def readFile(path: String): String = {
    val file = Source.fromFile(path)
    try {
      file.mkString
    } finally {
      file.close()
    }
  }
}
