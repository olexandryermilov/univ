package com.yermilov.commands

import com.yermilov.domain.Column
import com.yermilov.manager.{DatabaseManager, OutputManager}
import com.yermilov.utils.Parameters

object CreateTableCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val tableName = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    val databaseName = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    val columns = parameters.getOrElse(Parameters.columns, "").asInstanceOf[List[Column]]
    val key = parameters.getOrElse(Parameters.key, "").asInstanceOf[String]
    val table = databaseManager.createTable(tableName, columns, key, databaseName)
    outputManager.tableCreated(table)
  }
}
