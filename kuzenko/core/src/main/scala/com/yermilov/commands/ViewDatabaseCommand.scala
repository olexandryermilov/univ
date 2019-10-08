package com.yermilov.commands

import com.yermilov.manager.{DatabaseManager, OutputManager}
import com.yermilov.utils.Parameters

object ViewDatabaseCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val databaseName: String = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    val database = databaseManager.viewAllTables(databaseName)
    outputManager.databaseFound(database)
  }
}
