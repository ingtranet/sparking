package net.ingtra.sparking
package loader.input.kafka

import common.spark.{SparkConfig, createSparkSession, createStreamingContext}
import loader.input.Input

import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.spark.sql.DataFrame
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}

class KafkaStreamingInput(config: KafkaStreamingInputConfig)(implicit sparkConfig: SparkConfig) extends Input {
  override def forEachBatch(callback: (DataFrame, Long) => Any): Unit = {
    var kafkaParams = config.kafkaParams.asInstanceOf[Map[String, Object]]
    kafkaParams ++= Map[String, Object](
      "key.deserializer" -> classOf[ByteArrayDeserializer],
      "value.deserializer" -> classOf[ByteArrayDeserializer],
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val spark = createSparkSession(sparkConfig)
    val streamingContext = createStreamingContext(spark, config.batchDuration)
    val stream = KafkaUtils.createDirectStream[Array[Byte], Array[Byte]](
      streamingContext,
      PreferConsistent,
      Subscribe[Array[Byte], Array[Byte]](config.topics, kafkaParams)
    )
    var count = 1
    stream.foreachRDD { rdd =>
      import spark.implicits._
      val rowRdd = rdd.map { record =>
        (
          record.topic(),
          record.partition(),
          record.offset(),
          record.key(),
          record.value()
        )
      }
      val df = spark.createDataset(rowRdd)
        .toDF("topic", "partition", "offset", "key", "value")
      callback(df, count)
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
      count += 1
    }
    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
