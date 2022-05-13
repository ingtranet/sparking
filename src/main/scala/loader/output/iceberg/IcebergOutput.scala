package net.ingtra.sparking
package loader.output.iceberg

import common.udf.IcebergUdf
import loader.output.Output

import org.apache.spark.sql.DataFrame

class IcebergOutput(config: IcebergOutputConfig) extends Output {
  override def write(df: DataFrame, batchNumber: Long): Unit = {
    df.createOrReplaceTempView("this")
    val spark = df.sparkSession
    config.bucketUDFs.getOrElse(List.empty).foreach { udf =>
      IcebergUdf.registerBucketUDF(spark, udf.name, udf.`type`, udf.buckets)
    }
    config.sql.split(";").foreach(sql =>
      spark.sql(sql)
    )
    spark.catalog.dropTempView("this")
  }
}
