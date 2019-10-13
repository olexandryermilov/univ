package com.yermilov.manager

import com.yermilov.domain.{Column, Database, Row, Table}
import com.yermilov.utils.DBFileUtils

import scala.util.Try


class DatabaseManager(valueValidator: ValueValidator = new ValueValidator,
                      dbFileUtils: DBFileUtils = new DBFileUtils) {
  def createTable(tableName: String, columns: List[Column], key: String, databaseName: String): Table = {
    val table = Table(List.empty, tableName, columns.sortWith((a, b) => a.columnName.compareTo(b.columnName) < 0), key)
    dbFileUtils.saveTableTo(s"$databaseName/", table).getOrElse(println("Something went wrong"))
    table
  }

  def addRowToTable(columnsWithValues: List[(Column, String)], tableName: String, databaseName: String): Try[Row] = {
    valueValidator.validateValues(columnsWithValues) map { _ =>
      val sortedValues = columnsWithValues.sortWith(
        (a, b) => a._1.columnName.compareTo(b._1.columnName) < 0
      ).map(_._2)
      dbFileUtils.addRow(s"$databaseName/", tableName, sortedValues)
      Row(sortedValues)
    }
  }

  def dropTable(tableName: String, databaseName: String): Boolean =
    dbFileUtils.deleteTable(s"$databaseName/", tableName).map(_ => true).getOrElse(false)

  def findTable(tableName: String, databaseName: String): Try[Table] =
    dbFileUtils.readTable(s"$databaseName/", tableName)

  def removeRow(value: String, tableName: String, databaseName: String): Try[Unit] =
    dbFileUtils.removeRow(s"$databaseName/", tableName, value)

  def editRow(columnsAndValues: List[(Column, String)], tableName: String, databaseName: String): Try[Row] = {
    val table = findTable(tableName, databaseName).get
    val keyValue = columnsAndValues.find(cV => cV._1.columnName == table.key).getOrElse(throw new ShouldUseKeyColumnException)._2
    val keyIndex = table.columns.map(_.columnName).indexOf(table.key)
    val oldRow = table.columns.zip(table.rows.map(_.values).find(row => row(keyIndex) == keyValue).get)
    val allColumnsWithValue = table.columns.map(column =>
      column -> columnsAndValues.find(x => x._1 == column).map(_._2).getOrElse(oldRow.find(x => x._1 == column).map(_._2).getOrElse(""))
    )
    removeRow(keyValue, tableName, databaseName)
    addRowToTable(allColumnsWithValue, tableName, databaseName)
  }

  def viewAllTables(databaseName: String): Database =
    dbFileUtils.readDB(databaseName)

  def mergeTables(tableName1: String, tableName2: String, databaseName: String, joinOn: String): Try[Table] = {
    val firstTable = findTable(tableName1, databaseName).get
    val secondTable = findTable(tableName2, databaseName).get
    val newColumns = mergeColumns(firstTable.columns, secondTable.columns)
    val newRows: List[Row] = mergeRows(
      firstTable.rows.map(_.values.zip(firstTable.columns)),
      secondTable.rows.map(_.values.zip(secondTable.columns)),
      newColumns, joinOn
    ).map(Row)
    val newTable = Table(
      newRows,
      s"${firstTable.name}_merge_${secondTable.name}",
      newColumns,
      joinOn
    )
    dbFileUtils.saveTableTo(s"$databaseName/", newTable).map(_ => newTable)
  }

  def createDatabase(databaseName: String): Database =
    dbFileUtils.createDB(databaseName).get

  def deleteDatabase(databaseName: String): Unit =
    dbFileUtils.deleteDB(databaseName)

  def getAllDatabases: List[Database] =
    dbFileUtils.readAllDBs


  private def mergeColumns(firstTableColumns: List[Column], secondTableColumns: List[Column]): List[Column] =
    (firstTableColumns.toSet ++ secondTableColumns.toSet).toList.sortWith((a, b) => a.columnName.compareTo(b.columnName) < 0)

  private def mergeRows(firstTableRowsWithColumns: List[List[(String, Column)]],
                        secondTableRowsWithColumns: List[List[(String, Column)]],
                        newColumn: List[Column], joinOn: String): List[List[String]] = {
    val newValues: List[List[String]] = for {
      firstTableRow <- firstTableRowsWithColumns
      rowKey = firstTableRow.find(_._2.columnName == joinOn).get._1
    } yield
      secondTableRowsWithColumns.find(row => row.exists(r => r._2.columnName == joinOn && r._1 == rowKey)) match {
        case Some(r) => newColumn.map(col => firstTableRow
          .find(_._2.columnName == col.columnName)
          .map(_._1)
          .getOrElse(r.find(_._2.columnName == col.columnName).map(_._1).getOrElse(""))
        )

        case None => newColumn.map(col => firstTableRow.find(_._2.columnName == col.columnName).map(_._1).getOrElse(""))
      }
    newValues
  }

}

case class ShouldUseKeyColumnException() extends RuntimeException("Should have used key column when doing request")
