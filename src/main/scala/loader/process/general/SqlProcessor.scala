package net.ingtra.sparking
package loader.process.general

import loader.process.Processor

import org.apache.spark.sql.DataFrame

class SqlProcessor(config: SqlProcessorConfig) extends Processor {
  override def process(df: DataFrame, batchNumber: Long): DataFrame = {
    df.createOrReplaceTempView("this")
    val spark = df.sparkSession
    val result = spark.sql(config.sql)
    spark.catalog.dropTempView("this")
    result
  }
}
