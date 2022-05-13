package net.ingtra.sparking
package loader

import common.spark.SparkConfig

import io.circe.Json

case class ComponentConfig(
                            `type`: String,
                            config: Json
                          )

case class AppConfig(
                      spark: SparkConfig,
                      input: ComponentConfig,
                      processors: Option[List[ComponentConfig]],
                      outputs: List[ComponentConfig]
                    )
