package manager

import domain.{Column, Database, Row, Table}
import utils.DBFileUtils

import scala.util.Try

class DatabaseManager(valueValidator: ValueValidator = new ValueValidator) {
  def createTable(tableName: String, columns: Seq[Column], key: String, databaseName: String): Table = {
    val table = Table(Seq.empty, tableName, columns.sortWith((a, b) => a.columnName.compareTo(b.columnName) < 0), key)
    DBFileUtils.saveTableTo(s"$databaseName/", table).getOrElse(println("Something went wrong"))
    table
  }

  def addRowToTable(columnsWithValues: Seq[(Column, String)], tableName: String, databaseName: String): Try[Row] = {
    valueValidator.validateValues(columnsWithValues) map { _ =>
      val sortedValues = columnsWithValues.sortWith(
        (a, b) => a._1.columnName.compareTo(b._1.columnName) < 0
      ).map(_._2)
      DBFileUtils.addRow(s"$databaseName/", tableName, sortedValues)
      Row(sortedValues)
    }
  }

  def dropTable(tableName: String, databaseName: String): Boolean = {
    DBFileUtils.deleteTable(s"$databaseName/", tableName).map(_ => true).getOrElse(false)
  }

  def findTable(tableName: String, databaseName: String): Table = {
    DBFileUtils.readTable(s"$databaseName/", tableName).get
  }

  def removeRow(value: String, tableName: String, databaseName: String): Try[Unit] = {
    DBFileUtils.removeRow(s"$databaseName/", tableName, value)
  }

  def editRow(columnsAndValues: Seq[(Column, String)], tableName: String, databaseName: String): Try[Row] = {
    val table = findTable(tableName, databaseName)
    val keyValue = columnsAndValues.find(cV => cV._1.columnName == table.key).getOrElse(throw new ShouldUseKeyColumnException)._2
    val keyIndex = table.columns.map(_.columnName).indexOf(table.key)
    val oldRow = table.columns.zip(table.rows.map(_.values).find(row => row(keyIndex) == keyValue).get)
    val allColumnsWithValue = table.columns.map(column =>
      column -> columnsAndValues.find(x => x._1 == column).map(_._2).getOrElse(oldRow.find(x => x._1 == column).map(_._2).getOrElse(""))
    )
    removeRow(keyValue, tableName, databaseName)
    addRowToTable(allColumnsWithValue, tableName, databaseName)
  }

  def viewAllTables(databaseName: String): Database = {
    DBFileUtils.readDB(databaseName)
  }

  def mergeTables(tableName1: String, tableName2: String) = ???
}

case class ShouldUseKeyColumnException() extends RuntimeException("Should have use key column when doing request")
