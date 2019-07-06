package commands

import domain.Column
import manager.{DatabaseManager, OutputManager}
import utils.Parameters

object EditRowCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val columnsAndValues: Seq[(Column, String)] = parameters.getOrElse(Parameters.columnsAndValues, Seq.empty).asInstanceOf[Seq[(Column, String)]]
    val tableName: String = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    val databaseName: String = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    val row = databaseManager.editRow(columnsAndValues, tableName, databaseName)
    outputManager.rowEdited(tableName, row)
  }
}