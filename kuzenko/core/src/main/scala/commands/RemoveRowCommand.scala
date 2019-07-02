package commands

import domain.Column
import manager.{DatabaseManager, OutputManager}

class RemoveRowCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val columnsAndValues: Seq[(Column, String)] = parameters.getOrElse("columnsAndValues", Seq.empty).asInstanceOf[Seq[(Column, String)]]
    val tableName: String = parameters.getOrElse("name", "").asInstanceOf[String]
    databaseManager.removeRow(columnsAndValues, tableName)
    outputManager.rowRemoved(columnsAndValues, tableName)
  }
}
