package net.ingtra.sparking
package common.udf

import org.apache.iceberg.spark.IcebergSpark
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DataTypes

object IcebergUdf {
  def registerBucketUDF(spark: SparkSession, funcName: String, dataType: String, numBuckets: Int): Unit = {
    val dataTypeToRegister = dataType match {
      case "Integer" => DataTypes.IntegerType
      case "Long" => DataTypes.LongType
      case "Date" => DataTypes.DateType
      case "Timestamp" => DataTypes.TimestampType
      case "String" => DataTypes.StringType
      case "Binary" => DataTypes.BinaryType
    }
    IcebergSpark.registerBucketUDF(spark, funcName, dataTypeToRegister, numBuckets)
  }
}
