package com.yermilov.commands

import com.yermilov.manager.{DatabaseManager, OutputManager}
import com.yermilov.utils.Parameters

import scala.util.{Failure, Success}

object FindTableCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val tableName: String = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    val databaseName: String = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    val maybeTable = databaseManager.findTable(tableName, databaseName)
    maybeTable match {
      case Success(table)=> outputManager.tableFound (table)
      case Failure(exception)=> outputManager.failure(exception)
    }
  }
}
