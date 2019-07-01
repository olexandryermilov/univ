package manager

import domain.{Column, Table}

class DatabaseManager {
  def createTable(name: String, columns: Seq[Column]): Table = ???
}
