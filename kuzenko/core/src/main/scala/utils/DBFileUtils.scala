package utils

import java.io.File

import domain.{Column, Database, Row, Table, Type}
import org.apache.commons.io.FileUtils

import collection.JavaConverters._
import scala.util.Try

object DBFileUtils {

  import Parameters._
  import ScalaFileUtils._

  def saveTableTo(path: String, table: Table): Try[Unit] = Try {
    val columns = table.columns.map(col => col.columnName + " " + Type.toName(col.columnType)).mkString(",")
    writeToFile(tablePath(path, table.name), columns + "\n")
    val rows: String = table.rows.map(_.values.mkString(",")).mkString("\n")
    writeToFile(tablePath(path, table.name), rows, append = true)
    writeToFile(keyPath(path, table.name), table.key)
  }

  def deleteTable(path: String, tableName: String): Try[Unit] = Try {
    val fileToDelete = new File(tablePath(path, tableName))
    val newFile = new File(s"./deleted/$tableName$tableExtension")
    FileUtils.moveFile(fileToDelete, newFile)
  }

  def addRow(path: String, tableName: String, values: Seq[String]) = Try {
    val table = readTable(path, tableName).get
    val keyIndex = table.columns.map(_.columnName).indexOf(table.key)
    val hasKey = table.rows.map(_.values).find(row => row(keyIndex) == values(keyIndex))
    hasKey match {
      case Some(_) => {
        val newRows = table.rows.map(_.values).filter(row => row(keyIndex) != values(keyIndex)).map(values => Row(values))
        saveTableTo(path, table.copy(rows = newRows))
      }
      case None => writeToFile(tablePath(path, tableName), "\n" + values.mkString(","), true)
    }
  }

  def readTable(path: String, tableName: String): Try[Table] = Try {
    println(tablePath(path, tableName))
    println(path, tableName)
    val lines = readFile(tablePath(path, tableName)).filterNot(_.isEmpty)
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

    val key = readFile(keyPath(path, tableName)).head
    val table = Table(rows, tableName, columns, key)
    table
  }

  def readDB(databaseName: String): Database =
    Database(
      FileUtils
        .listFiles(new File(s"$dbLocation/$databaseName/"), Seq(tableExtension.drop(1)).toArray[String], false)
        .asScala.map(fileName => readTable(s"$databaseName/", fileName.getName.dropRight(tableExtension.length)).get)
        .toSeq,
      databaseName
    )

  private def tablePath(path: String, tableName: String): String = s"$dbLocation$path$tableName$tableExtension"

  private def keyPath(path: String, tableName: String): String = s"$dbLocation$path$tableName$keyExtension"
}
