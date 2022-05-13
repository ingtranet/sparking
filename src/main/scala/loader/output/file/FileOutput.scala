package net.ingtra.sparking
package loader.output.file

import loader.output.Output

import org.apache.spark.sql.{DataFrame, SaveMode}

class FileOutput(config: FileOutputConfig) extends Output {
  val fileType = FileTypes.withName(config.fileType)
  val saveMode = config.saveMode.getOrElse("default") match {
    case "overwrite" => SaveMode.Overwrite
    case "append" => SaveMode.Append
    case "default" => SaveMode.ErrorIfExists
  }

  override def write(df: DataFrame, batchNumber: Long): Unit = {
    FileTypes.withName(config.fileType) match {
      case FileTypes.`json` =>
        df.write.mode(saveMode).json(config.path)
      case FileTypes.`csv` =>
        df.write.mode(saveMode).csv(config.path)
    }
  }
}
