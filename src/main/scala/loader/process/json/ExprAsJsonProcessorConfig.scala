package net.ingtra.sparking
package loader.process.json

case class ExprAsJsonProcessorConfig(
                                      expr: String,
                                      schemaFromTable: Option[String]
                                    )
