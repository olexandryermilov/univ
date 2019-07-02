package commands

import domain.Column
import manager.{DatabaseManager, OutputManager}
import utils.Parameters

class AddRowCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val columnsWithValues: Seq[(Column, String)] = parameters.getOrElse(Parameters.columnsAndValues, Seq.empty).asInstanceOf[Seq[(Column, String)]]
    val tableName: String = parameters.getOrElse("name", "").asInstanceOf[String]
    val row = databaseManager.addRowToTable(columnsWithValues, tableName)
    outputManager.rowAdded(tableName, row)
  }
}
