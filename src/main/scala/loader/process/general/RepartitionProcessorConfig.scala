package net.ingtra.sparking
package loader.process.general

case class RepartitionProcessorConfig(
                                       number: Option[Int],
                                       exprs: Option[List[String]]
                                     )
