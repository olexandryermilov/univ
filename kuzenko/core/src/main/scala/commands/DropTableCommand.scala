package commands

import manager.{DatabaseManager, OutputManager}
import utils.Parameters

object DropTableCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val tableName = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    val databaseName = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    databaseManager.dropTable(tableName, databaseName)
    outputManager.tableDropped(tableName)
  }
}
