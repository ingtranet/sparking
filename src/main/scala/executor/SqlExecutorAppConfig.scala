package net.ingtra.sparking
package executor

import common.spark.SparkConfig

case class SqlExecutorAppConfig(
                                 spark: SparkConfig,
                                 sql: String
                               )
