package net.ingtra.sparking
package loader

import common.config.ConfigParser
import common.spark._
import loader.input.Input
import loader.input.file.{FileInput, FileInputConfig}
import loader.input.kafka.{KafkaBatchInput, KafkaBatchInputConfig, KafkaStreamingInput, KafkaStreamingInputConfig}
import loader.output.Output
import loader.output.file.{FileOutput, FileOutputConfig}
import loader.output.iceberg.{IcebergAppendDeduplicatedOutput, IcebergAppendDeduplicatedOutputConfig, IcebergOutput, IcebergOutputConfig}
import loader.process.Processor
import loader.process.general._
import loader.process.json.{ExprAsJsonProcessor, ExprAsJsonProcessorConfig}

import io.circe.Json
import io.circe.generic.auto._


object LoaderApp {
  def main(args: Array[String]): Unit = {
    val url = args(0)
    val config = new ConfigParser(url).getAs[AppConfig]
    implicit val sparkConfig: SparkConfig = config.spark

    val input = getInput(config.input.`type`, config.input.config)
    val processors = config.processors.getOrElse(List.empty[ComponentConfig]).map(p => getProcessor(p.`type`, p.config))
    val outputs = config.outputs.map(o => getOutput(o.`type`, o.config))

    startPipeline(input, processors, outputs)
  }

  private def startPipeline(input: Input, processors: Seq[Processor], outputs: Seq[Output]): Unit = {
    input.forEachBatch { (batchDf, batchNumber) =>
      var dfToWrite = batchDf
      processors.foreach { p =>
        dfToWrite = p.process(dfToWrite, batchNumber)
      }
      if (outputs.length == 1) {
        outputs.head.write(dfToWrite, batchNumber)
      } else {
        dfToWrite.persist()
        outputs.foreach(_.write(dfToWrite, batchNumber))
        dfToWrite.unpersist(true)
      }
    }
  }

  private def getInput(typeName: String, config: Json)(implicit sparkConfig: SparkConfig): Input = {
    typeName match {
      case "KafkaStreaming" => new KafkaStreamingInput(config.as[KafkaStreamingInputConfig].right.get)
      case "KafkaBatch" => new KafkaBatchInput(config.as[KafkaBatchInputConfig].right.get)
      case "File" => new FileInput(config.as[FileInputConfig].right.get)
    }
  }

  private def getProcessor(typeName: String, config: Json): Processor = {
    typeName match {
      case "ExprAsJson" => new ExprAsJsonProcessor(config.as[ExprAsJsonProcessorConfig].right.get)
      case "Repartition" => new RepartitionProcessor(config.as[RepartitionProcessorConfig].right.get)
      case "Coalesce" => new CoalesceProcessor(config.as[CoalesceProcessorConfig].right.get)
      case "Sql" => new SqlProcessor(config.as[SqlProcessorConfig].right.get)
      case "DropDuplicates" => new DropDuplicatesProcessor(config.as[DropDuplicatesProcessorConfig].right.get)
    }
  }

  private def getOutput(typeName: String, config: Json): Output = {
    typeName match {
      case "File" => new FileOutput(config.as[FileOutputConfig].right.get)
      case "Iceberg" => new IcebergOutput(config.as[IcebergOutputConfig].right.get)
      case "IcebergAppendDeduplicated" => new IcebergAppendDeduplicatedOutput(config.as[IcebergAppendDeduplicatedOutputConfig].right.get)
    }
  }
}
