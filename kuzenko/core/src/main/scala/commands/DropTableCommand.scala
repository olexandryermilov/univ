package commands

import manager.{DatabaseManager, OutputManager}
import utils.Parameters

class DropTableCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val name = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    databaseManager.dropTable(name)
    outputManager.tableDropped(name)
  }
}
