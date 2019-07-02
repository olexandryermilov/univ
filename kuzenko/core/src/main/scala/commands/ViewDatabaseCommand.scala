package commands

import manager.{DatabaseManager, OutputManager}

class ViewDatabaseCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val database = databaseManager.viewAllTables()
    outputManager.databaseFound(database)
  }
}
