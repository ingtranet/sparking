package net.ingtra.sparking
package common.spark

case class SparkConfig(
                        appName: String,
                        master: Option[String],
                        config: Option[Map[String, String]]
                      )
