package com.yermilov.utils

import java.io.File
import java.util

import com.yermilov.domain.{Column, Database, Row, Table, Type}
import com.yermilov.utils.Parameters.dbLocation
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter

import collection.JavaConverters._
import scala.util.Try

class DBFileUtils {

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
    deleteFile(tablePath(path, tableName), tableName, tableExtension)
    deleteFile(keyPath(path, tableName), tableName, keyExtension)
  }

  def addRow(path: String, tableName: String, newRow: List[String]): Try[Unit] = Try {
    val table = readTable(path, tableName).get
    val keyIndex = table.columns.map(_.columnName).indexOf(table.key)
    val hasKey = table.rows.map(_.values).find(row => row(keyIndex) == newRow(keyIndex))
    hasKey match {
      case Some(_) => {
        val newRows = table.rows.map(_.values).filter(row => row(keyIndex) != newRow(keyIndex)).map(values => Row(values)) ++ List(Row(newRow))
        saveTableTo(path, table.copy(rows = newRows))
      }
      case None => writeToFile(tablePath(path, tableName), "\n" + newRow.mkString(","), true)
    }
  }

  def readTable(path: String, tableName: String): Try[Table] = Try {
    val lines = readFile(tablePath(path, tableName)).filterNot(_.isEmpty)
    val columns = lines.head.split(",").map(
      column => {
        val splitted = column.split(" ").reverse
        Column(columnName = splitted.tail.reverse.mkString(" "), columnType = Type.toType(splitted.head))
      }
    ).toList
    val rows = lines.tail.map(
      line =>
        Row(line.split(",").toList)
    )

    val key = readFile(keyPath(path, tableName)).head
    val table = Table(rows, tableName, columns, key)
    table
  }

  def readDB(databaseName: String): Database =
    Database(
      FileUtils
        .listFiles(new File(s"$dbLocation/$databaseName/"), List(tableExtension.drop(1)).toArray[String], false)
        .asScala.map(fileName => readTable(s"$databaseName/", fileName.getName.dropRight(tableExtension.length)).get)
        .toList,
      databaseName
    )

  def readAllDBs: List[Database] = new File(s"$dbLocation").list().filterNot(_.contains("."))
    .map(readDB).toList

  def createDB(databaseName: String): Try[Database] = Try {
    FileUtils.touch(new File(s"$dbLocation$databaseName/"))
    Database(List.empty, databaseName)
  }

  def removeRow(path: String, tableName: String, value: String): Try[Unit] = Try {
    val table = readTable(path, tableName).get
    val keyIndex = table.columns.map(_.columnName).indexOf(table.key)
    val newTable: Table = table.copy(rows = table.rows.map(_.values).filterNot(row => row(keyIndex) == value).map(Row))
    if (newTable != table) saveTableTo(path, newTable)
  }

  private def tablePath(path: String, tableName: String): String = s"$dbLocation$path$tableName$tableExtension"

  private def keyPath(path: String, tableName: String): String = s"$dbLocation$path$tableName$keyExtension"
}

object DBFileUtils {

  import collection.JavaConverters._

  def main(args: Array[String]): Unit = {

  }
}
