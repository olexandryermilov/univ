package manager

import domain.{Column, Database, Row, Table}

class OutputManager {
  def tableCreated(table: Table): Unit = println(s"Created table $table")

  def rowAdded(name: String, row: Row): Unit = println(s"Added $row to $name")

  def rowEdited(name: String, row: Row): Unit = ???

  def tableDropped(name: String): Unit = println(s"Table $name dropped.")

  def rowRemoved(columnsAndValues: Seq[(Column, String)], tableName: String): Unit = ???

  def tableFound(table: Table): Unit = println(s"Table found $table")

  def databaseFound(database: Database): Unit = println(s"Database found $database")

  def mergeTables(tableName1: String, tableName2: String): Unit = println(s"Tables merged")

  def failure(e: Throwable): Unit = println(e.getMessage)

}
