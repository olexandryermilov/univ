package manager

import domain.{Column, Database, Row, Table}
import utils.DBFileUtils

class DatabaseManager {
  def createTable(tableName: String, columns: Seq[Column], key: String, databaseName: String): Table = {
    val table = Table(Seq.empty, tableName, columns, key)
    DBFileUtils.saveTableTo(s"$databaseName/", table).getOrElse(println("Something went wrong"))
    table
  }

  def addRowToTable(columnsWithValues: Seq[(Column, String)], name: String, databaseName: String): Row = {
    ???
  }

  def dropTable(tableName: String, databaseName: String): Boolean = {
    DBFileUtils.deleteTable(s"$databaseName/", tableName).map(_ => true).getOrElse(false)
  }

  def findTable(tableName: String, databaseName: String): Table = {
    DBFileUtils.readTable(s"$databaseName/", tableName).get
  }

  def removeRow(columnsAndValues: Seq[(Column, String)], tableName: String, databaseName: String): Row = ???

  def editRow(columnsAndValues: Seq[(Column, String)], tableName: String, databaseName: String): Row = ???

  def viewAllTables(databaseName: String): Database = ???
}
