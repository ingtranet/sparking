package net.ingtra.sparking
package loader.process.json

import loader.process.Processor

import org.apache.spark.sql.DataFrame

class ExprAsJsonProcessor(config: ExprAsJsonProcessorConfig) extends Processor {
  override def process(df: DataFrame, batchNumber: Long): DataFrame = {
    val spark = df.sparkSession
    import spark.implicits._

    val inputDS = df.selectExpr(config.expr).as[String]
    if (config.schemaFromTable.isDefined) {
      val schema = spark.read.table(config.schemaFromTable.get).limit(0).schema
      spark.read.schema(schema).json(inputDS)
    } else {
      spark.read.json(inputDS)
    }
  }
}
