package net.ingtra.sparking
package common.config

class InvalidConfigException(message: String, cause: Throwable) extends Exception(message: String, cause: Throwable) {
  def this(message: String) = this(message, null)

  def this(cause: Throwable) = this(null, cause)
}