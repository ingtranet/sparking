package net.ingtra.sparking
package common

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming._

package object spark {
  def createSparkSession(sparkOption: SparkConfig): SparkSession = {
    var builder = SparkSession.builder()

    builder = builder.appName(sparkOption.appName)

    if (sparkOption.master.isDefined) {
      builder = builder.master(sparkOption.master.get)
    }

    sparkOption.config.getOrElse(Map.empty[String, String]).foreach { case (k, v) =>
      builder = builder.config(k, v)
    }

    builder.getOrCreate()
  }

  def createStreamingContext(sparkSession: SparkSession, batchDuration: Long): StreamingContext = {
    new StreamingContext(sparkSession.sparkContext, Seconds(batchDuration))
  }
}
