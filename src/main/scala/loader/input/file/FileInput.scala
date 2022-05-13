package net.ingtra.sparking
package loader.input.file

import common.spark._
import loader.input.Input

import org.apache.spark.sql.DataFrame

class FileInput(config: FileInputConfig)(implicit sparkConfig: SparkConfig) extends Input {
  private val fileType = FileTypes.withName(config.fileType)
  private val spark = createSparkSession(sparkConfig)

  override def forEachBatch(callback: (DataFrame, Long) => Any): Unit = {
    val df = fileType match {
      case FileTypes.`json` =>
        spark.read.json(config.path)
      case FileTypes.`csv` =>
        spark.read.csv(config.path)
    }
    callback(df, 1)
  }
}
