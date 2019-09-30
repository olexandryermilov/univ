package utils

import java.io.File
import java.nio.charset.StandardCharsets

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import domain.{Column, Database, Row, Table, Type}
import org.apache.commons.io.FileUtils

import collection.JavaConverters._
import scala.util.Try

object DBFileUtils {

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  import Parameters._

  def saveTableTo(path: String, table: Table): Try[Unit] = Try {
    val tablePath = dbLocation + path + table.name + extension
    val file = new File(tablePath)
    FileUtils.touch(file)
    val columns = table.columns.map(col => col.columnName + " " + Type.toName(col.columnType)).mkString(",")
    FileUtils.writeStringToFile(file, columns + "\n", StandardCharsets.UTF_8.name())
    val rows: String = table.rows.map(_.values.mkString(",")).mkString("\n")
    FileUtils.writeStringToFile(file, rows, StandardCharsets.UTF_8.name(), true)
  }

  def deleteTable(path: String, tableName: String): Try[Unit] = Try {
    val tablePath = dbLocation + path + tableName + extension
    val fileToDelete = new File(tablePath)
    val newFile = new File(s"deleted/$tableName$extension")
    FileUtils.moveFile(fileToDelete, newFile)
  }

  def readTable(path: String, tableName: String): Try[Table] = Try {
    val tablePath = dbLocation + path + tableName + extension
    val fileToRead = new File(tablePath)
    val lines = FileUtils.readLines(fileToRead, StandardCharsets.UTF_8.name()).asScala
    val columns = lines.head.split(",").map(
      column => {
        val splitted = column.split(" ").reverse
        Column(columnName = splitted.tail.reverse.mkString(" "), columnType = Type.toType(splitted.head))
      }
    )
    val rows = lines.tail.map(
      line =>
        Row(line.split(","))
    )
    val table = Table(rows, tableName, columns, "key")
    table
  }

  def readDB(databaseName: String): Database =
    Database(
      FileUtils
        .listFiles(new File(s"$dbLocation/$databaseName/"), null, false)
        .asScala.map(fileName => readTable(s"$databaseName/", fileName.getName.dropRight(extension.length)).get)
        .toSeq,
      databaseName
    )

}
