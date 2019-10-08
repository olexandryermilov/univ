package com.yermilov.commands

import com.yermilov.domain.Column
import com.yermilov.manager.{DatabaseManager, OutputManager}
import com.yermilov.utils.Parameters

import scala.util.{Failure, Success}

object AddRowCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val columnsWithValues: List[(Column, String)] = parameters.getOrElse(Parameters.columnsAndValues, List.empty).asInstanceOf[List[(Column, String)]]
    val tableName: String = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    val databaseName: String = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    val maybeRow = databaseManager.addRowToTable(columnsWithValues, tableName, databaseName)
    maybeRow match {
      case Success(row) => outputManager.rowAdded(tableName, row)
      case Failure(exception) => outputManager.failure(exception)
    }
  }
}
