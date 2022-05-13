package net.ingtra.sparking
package loader.input

import org.apache.spark.sql.DataFrame

trait Input {
  def forEachBatch(callback: (DataFrame, Long) => Any): Unit
}
