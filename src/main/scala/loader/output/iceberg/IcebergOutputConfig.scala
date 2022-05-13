package net.ingtra.sparking
package loader.output.iceberg

case class IcebergOutputBucketUdfConfig(
                                         name: String,
                                         `type`: String,
                                         buckets: Int
                                       )

case class IcebergOutputConfig(
                                bucketUDFs: Option[List[IcebergOutputBucketUdfConfig]],
                                sql: String
                              )