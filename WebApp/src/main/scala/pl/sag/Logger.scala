package pl.sag

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

trait Logger {

  val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  def info(msg: String) = logger.info(msg)
  def debug(msg: String) = logger.debug(msg)
  def error(msg: String) = logger.error(msg)

}