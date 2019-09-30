package utils

import java.io.File
import java.nio.charset.StandardCharsets
import collection.JavaConverters._
import org.apache.commons.io.FileUtils

object ScalaFileUtils {

  def writeToFile(filePath: String, text: String, append: Boolean = false): Unit = {
    FileUtils.touch(new File(filePath))
    FileUtils.writeStringToFile(new File(filePath), text, StandardCharsets.UTF_8.name(), append)
  }

  def readFile(filePath: String): Seq[String] = FileUtils.readLines(new File(filePath), StandardCharsets.UTF_8.name()).asScala
}
