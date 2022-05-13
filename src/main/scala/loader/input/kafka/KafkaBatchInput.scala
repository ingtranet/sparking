package net.ingtra.sparking
package loader.input.kafka

import common.spark.{SparkConfig, createSparkSession}
import loader.input.Input

import org.apache.spark.sql.DataFrame

class KafkaBatchInput(config: KafkaBatchInputConfig)(implicit sparkConfig: SparkConfig) extends Input {
  override def forEachBatch(callback: (DataFrame, Long) => Any): Unit = {
    val spark = createSparkSession(sparkConfig)
    val df = spark
      .read
      .format("kafka")
      .options(config.options)
      .load()

    callback(df, 1)
  }
}
