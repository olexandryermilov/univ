package manager

import domain.{Column, Database, Row, Table}

class OutputManager {
  def tableCreated(table: Table): Unit = ???

  def rowAdded(name: String, row: Row): Unit = ???

  def rowEdited(name: String, row: Row): Unit = ???

  def tableDropped(name: String): Unit = ???

  def rowRemoved(columnsAndValues: Seq[(Column, String)], tableName: String): Unit = ???

  def tableFound(table: Table): Unit = ???

  def databaseFound(database: Database): Unit = ???

}
