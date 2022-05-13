package net.ingtra.sparking
package loader.process.general

import common.config.InvalidConfigException
import loader.process.Processor

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

class RepartitionProcessor(config: RepartitionProcessorConfig) extends Processor {
  override def process(df: DataFrame, batchNumber: Long): DataFrame = {
    if (config.number.isDefined && config.exprs.isDefined)
      df.repartition(config.number.get, config.exprs.get.map(expr): _*)
    else if (config.number.isDefined)
      df.repartition(config.number.get)
    else if (config.exprs.isDefined)
      df.repartition(config.exprs.get.map(expr): _*)
    else
      throw new InvalidConfigException("invalid repartition config")
  }
}
