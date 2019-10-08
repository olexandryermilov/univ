package com.yermilov.commands

import com.yermilov.domain.Column
import com.yermilov.manager.{DatabaseManager, OutputManager}
import com.yermilov.utils.Parameters

object EditRowCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val columnsAndValues: List[(Column, String)] = parameters.getOrElse(Parameters.columnsAndValues, List.empty).asInstanceOf[List[(Column, String)]]
    val tableName: String = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    val databaseName: String = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    val row = databaseManager.editRow(columnsAndValues, tableName, databaseName)
    outputManager.rowEdited(tableName, row.get)
  }
}
