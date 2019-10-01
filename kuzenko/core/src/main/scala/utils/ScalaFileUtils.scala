package utils

import java.io.File
import java.nio.charset.StandardCharsets

import collection.JavaConverters._
import org.apache.commons.io.FileUtils

import scala.util.Try

object ScalaFileUtils {

  def writeToFile(filePath: String, text: String, append: Boolean = false): Unit = {
    FileUtils.touch(new File(filePath))
    FileUtils.writeStringToFile(new File(filePath), text, StandardCharsets.UTF_8.name(), append)
  }

  def readFile(filePath: String): Seq[String] = FileUtils.readLines(new File(filePath), StandardCharsets.UTF_8.name()).asScala

  def deleteFile(filePath: String, tableName: String, extension: String): Unit = {
    val fileToDelete = new File(filePath)
    val newFile = new File(s"./deleted/$tableName$extension")
    Try(FileUtils.forceDelete(newFile))
    FileUtils.moveFile(fileToDelete, newFile)
  }
}
