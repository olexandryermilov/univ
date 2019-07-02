package commands

import manager.{DatabaseManager, OutputManager}

class DropTableCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val name = parameters.getOrElse("name", "").asInstanceOf[String]
    databaseManager.dropTable(name)
    outputManager.tableDropped(name)
  }
}
