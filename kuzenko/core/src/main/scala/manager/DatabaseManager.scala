package manager

import domain.{Column, Database, Row, Table}

class DatabaseManager {
  def createTable(name: String, columns: Seq[Column], key: String): Table = ???

  def addRowToTable(columnsWithValues: Seq[(Column, String)], name: String): Row = ???

  def dropTable(name: String): Boolean = ???

  def findTable(name: String): Table = ???

  def removeRow(columnsAndValues: Seq[(Column, String)], name: String): Row = ???

  def editRow(columnsAndValues: Seq[(Column, String)], name: String): Row = ???

  def viewAllTables(): Database = ???
}
