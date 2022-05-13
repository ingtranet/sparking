package net.ingtra.sparking
package loader.process

import org.apache.spark.sql.DataFrame

trait Processor {
  def process(df: DataFrame, batchNumber: Long): DataFrame
}
