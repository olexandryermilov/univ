package commands

import manager.{DatabaseManager, OutputManager}
import utils.Parameters

class FindTableCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val tableName: String = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    val databaseName: String = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    val table = databaseManager.findTable(tableName, databaseName)
    outputManager.tableFound(table)
  }
}
