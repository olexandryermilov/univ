package manager

import domain.{Column, Database, Row, Table}

class DatabaseManager {
  def createTable(tableName: String, columns: Seq[Column], key: String, databaseName: String): Table = ???

  def addRowToTable(columnsWithValues: Seq[(Column, String)], name: String, databaseName: String): Row = ???

  def dropTable(tableName: String, databaseName: String): Boolean = ???

  def findTable(talbleName: String, databaseName: String): Table = ???

  def removeRow(columnsAndValues: Seq[(Column, String)], tableName: String, databaseName: String): Row = ???

  def editRow(columnsAndValues: Seq[(Column, String)], tableName: String, databaseName: String): Row = ???

  def viewAllTables(databaseName: String): Database = ???
}
