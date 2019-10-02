import commands.{AddRowCommand, CreateTableCommand, DropTableCommand, EditRowCommand, FindTableCommand, RemoveRowCommand, ViewDatabaseCommand}
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
        Parameters.key -> "column2"
      ),
      databaseManager,
      outputManager
    )
    AddRowCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table",
        Parameters.databaseName -> "db",
        Parameters.columnsAndValues ->
          Seq[(Column, String)](
            Column(Type.Integer, "column1") -> "15",
            Column(Type.Integer, "column2") -> "32"
          )
      ),
      databaseManager,
      outputManager
    )
    AddRowCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table",
        Parameters.databaseName -> "db",
        Parameters.columnsAndValues ->
          Seq[(Column, String)](
            Column(Type.Integer, "column1") -> "15",
            Column(Type.Integer, "column2") -> "33"
          )
      ),
      databaseManager,
      outputManager
    )
    AddRowCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table",
        Parameters.databaseName -> "db",
        Parameters.columnsAndValues ->
          Seq[(Column, String)](
            Column(Type.Integer, "column1") -> "15444",
            Column(Type.Integer, "column2") -> "32"
          )
      ),
      databaseManager,
      outputManager
    )
    AddRowCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table",
        Parameters.databaseName -> "db",
        Parameters.columnsAndValues ->
          Seq[(Column, String)](
            Column(Type.Integer, "column1") -> "1reg",
            Column(Type.Integer, "column2") -> "32"
          )
      ),
      databaseManager,
      outputManager
    )
    RemoveRowCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table",
        Parameters.databaseName -> "db",
        Parameters.row -> "33"
      ),
      databaseManager,
      outputManager
    )
    EditRowCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table",
        Parameters.databaseName -> "db",
        Parameters.columnsAndValues ->
          Seq[(Column, String)](
            Column(Type.Integer, "column1") -> "154",
            Column(Type.Integer, "column2") -> "32"
          )
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
