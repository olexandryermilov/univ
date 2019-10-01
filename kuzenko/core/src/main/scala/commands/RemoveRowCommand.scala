package commands

import manager.{DatabaseManager, OutputManager}
import utils.Parameters

import scala.util.{Failure, Success}

object RemoveRowCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val value: String = parameters.getOrElse(Parameters.row, Seq.empty).asInstanceOf[String]
    val tableName: String = parameters.getOrElse(Parameters.tableName, "").asInstanceOf[String]
    val databaseName: String = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    val result = databaseManager.removeRow(value, tableName, databaseName)
    result match {
      case Success(_) => outputManager.rowRemoved(value, tableName)
      case Failure(exception) => outputManager.failure(exception)
    }
  }
}
