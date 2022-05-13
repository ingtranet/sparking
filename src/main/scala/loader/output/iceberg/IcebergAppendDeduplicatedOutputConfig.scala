package net.ingtra.sparking
package loader.output.iceberg


case class IcebergAppendDeduplicatedOutputConfig(
                                                  table: String,
                                                  keys: List[String],
                                                  partitions: Option[List[String]]
                                                )