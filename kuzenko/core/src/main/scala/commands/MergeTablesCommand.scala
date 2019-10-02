package commands
import manager.{DatabaseManager, OutputManager}
import utils.Parameters

import scala.util.{Failure, Success}

object MergeTablesCommand extends Command {
  override def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager): Unit = {
    val firstTableName: String = parameters.getOrElse(Parameters.firstTableName, "").asInstanceOf[String]
    val secondTableName: String = parameters.getOrElse(Parameters.secondTableName, "").asInstanceOf[String]
    val databaseName: String = parameters.getOrElse(Parameters.databaseName, "").asInstanceOf[String]
    val joinOn: String = parameters.getOrElse(Parameters.joinOn, "").asInstanceOf[String]
    val result = databaseManager.mergeTables(firstTableName, secondTableName, databaseName, joinOn)
    result match {
      case Success(table) => outputManager.mergeTables(firstTableName, secondTableName, table)
      case Failure(exception) => outputManager.failure(exception)
    }
  }
}
