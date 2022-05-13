package net.ingtra.sparking
package executor

import common.config.ConfigParser
import loader.AppConfig

import io.circe.generic.auto._
import net.ingtra.sparking.common.spark.createSparkSession

class SqlExecutorApp {
  def main(args: Array[String]): Unit = {
    val url = args(0)
    val config = new ConfigParser(url).getAs[SqlExecutorAppConfig]

    val spark = createSparkSession(config.spark)
    spark.sql(config.sql)
  }
}
