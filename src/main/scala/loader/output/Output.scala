package net.ingtra.sparking
package loader.output

import org.apache.spark.sql.DataFrame

trait Output {
  def write(df: DataFrame, batchNumber: Long): Unit
}
