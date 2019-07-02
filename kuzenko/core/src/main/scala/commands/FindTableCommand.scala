package commands

import manager.{DatabaseManager, OutputManager}
import utils.Parameters

class FindTableCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val tableName: String = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    val table = databaseManager.findTable(tableName)
    outputManager.tableFound(table)
  }
}
