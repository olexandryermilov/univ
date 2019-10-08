import com.yermilov.commands.{AddRowCommand, CreateTableCommand, DropTableCommand, EditRowCommand, FindTableCommand, MergeTablesCommand, RemoveRowCommand, ViewDatabaseCommand}
import com.yermilov.domain.{Column, Type}
import com.yermilov.manager.{DatabaseManager, OutputManager}
import com.yermilov.utils.Parameters

object Main {
  def main(args: Array[String]): Unit = {
    val databaseManager = new DatabaseManager()
    val outputManager = new OutputManager
    CreateTableCommand.run(
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
          List[(Column, String)](
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
          List[(Column, String)](
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
          List[(Column, String)](
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
          List[(Column, String)](
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
          List[(Column, String)](
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

    CreateTableCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table2",
        Parameters.databaseName -> "db",
        Parameters.columns -> List(
          Column(Type.Integer, "column3"),
          Column(Type.Integer, "column2")
        ),
        Parameters.key -> "column2"
      ),
      databaseManager,
      outputManager
    )
    AddRowCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table2",
        Parameters.databaseName -> "db",
        Parameters.columnsAndValues ->
          List[(Column, String)](
            Column(Type.Integer, "column3") -> "1",
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
          List[(Column, String)](
            Column(Type.Integer, "column1") -> "100",
            Column(Type.Integer, "column2") -> "323"
          )
      ),
      databaseManager,
      outputManager
    )
    AddRowCommand.run(
      Map[String, Any](
        Parameters.tableName -> "table2",
        Parameters.databaseName -> "db",
        Parameters.columnsAndValues ->
          List[(Column, String)](
            Column(Type.Integer, "column3") -> "100",
            Column(Type.Integer, "column2") -> "3"
          )
      ),
      databaseManager,
      outputManager
    )
    MergeTablesCommand.run(
      Map[String, Any](
        Parameters.firstTableName -> "table",
        Parameters.secondTableName -> "table2",
        Parameters.databaseName -> "db",
        Parameters.joinOn -> "column2"
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
