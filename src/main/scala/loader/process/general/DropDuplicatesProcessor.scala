package net.ingtra.sparking
package loader.process.general

import loader.process.Processor

import org.apache.spark.sql.DataFrame

class DropDuplicatesProcessor(config: DropDuplicatesProcessorConfig) extends Processor {
  override def process(df: DataFrame, batchNumber: Long): DataFrame = {
    df.dropDuplicates(config.columns)
  }
}
