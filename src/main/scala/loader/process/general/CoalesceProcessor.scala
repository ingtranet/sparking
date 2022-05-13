package net.ingtra.sparking
package loader.process.general

import loader.process.Processor

import org.apache.spark.sql.DataFrame

class CoalesceProcessor(config: CoalesceProcessorConfig) extends Processor {
  override def process(df: DataFrame, batchNumber: Long): DataFrame = {
    df.coalesce(config.number)
  }
}
