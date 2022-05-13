package net.ingtra.sparking
package loader.output.file

case class FileOutputConfig(
                             path: String,
                             fileType: String,
                             saveMode: Option[String]
                           )
