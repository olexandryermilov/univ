import commands.{CreateTableCommand, DropTableCommand, FindTableCommand, ViewDatabaseCommand}
import domain.{Column, Type}
import manager.{DatabaseManager, OutputManager}
import utils.Parameters

object Main {
  def main(args: Array[String]): Unit = {
    val databaseManager = new DatabaseManager
    val outputManager = new OutputManager
    val command = CreateTableCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table",
        Parameters.databaseName -> "db",
        Parameters.columns -> List(
          Column(Type.Integer, "column1"),
          Column(Type.Integer, "column2")
        ),
        Parameters.key -> "key"
      ),
      databaseManager,
      outputManager
    )
    FindTableCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table",
        Parameters.databaseName -> "db"
      ),
      databaseManager,
      outputManager
    )
    ViewDatabaseCommand.run(
      Map[String, Any](
        Parameters.databaseName -> "db"
      ),
      databaseManager,
      outputManager
    )
    DropTableCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table",
        Parameters.databaseName -> "db"
      ),
      databaseManager,
      outputManager
    )
  }
}
