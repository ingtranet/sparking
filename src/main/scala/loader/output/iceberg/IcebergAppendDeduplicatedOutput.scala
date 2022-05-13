package net.ingtra.sparking
package loader.output.iceberg


import loader.output.Output

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

class IcebergAppendDeduplicatedOutput(config: IcebergAppendDeduplicatedOutputConfig) extends Output {
  override def write(df: DataFrame, batchNumber: Long): Unit = {
    implicit val spark: SparkSession = df.sparkSession

    val inputDF = df.dropDuplicates(config.keys).persist()
    var table = spark.read.format("iceberg").load(config.table)
    config.partitions.getOrElse(List.empty).foreach(p => {
      val values = inputDF.select(p).distinct().collect().map(_.get(0))
      table = table.filter(col(p).isInCollection(values))
    })
    inputDF
      .join(table, config.keys, "anti")
      .repartition(1)
      .writeTo(config.table)
      .append()
    inputDF.unpersist(true)
  }
}
