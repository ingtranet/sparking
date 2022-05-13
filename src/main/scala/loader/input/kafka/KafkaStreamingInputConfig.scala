package net.ingtra.sparking
package loader.input.kafka

case class KafkaStreamingInputConfig(
                                      topics: List[String],
                                      kafkaParams: Map[String, String],
                                      batchDuration: Long
                                    )
