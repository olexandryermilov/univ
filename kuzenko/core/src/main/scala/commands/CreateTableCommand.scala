package commands

import domain.Column
import manager.{DatabaseManager, OutputManager}

class CreateTableCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val name = parameters.getOrElse("name", "").asInstanceOf[String]
    val columns = parameters.getOrElse("columns", "").asInstanceOf[List[Column]]
    val table = databaseManager.createTable(name, columns)
    outputManager.tableCreated(table)
  }
}
