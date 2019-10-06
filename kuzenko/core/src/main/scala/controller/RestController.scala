package controller


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import domain._
import manager.{DatabaseManager, OutputManager}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

@Controller
class RestController(databaseManager: DatabaseManager, outputManager: OutputManager) {

  import Mappers._

  @RequestMapping(value = Array("/{databaseName}/table"), method = Array(RequestMethod.POST))
  @ResponseBody def createTable(@RequestBody request: CreateTableRequest, @PathVariable databaseName: String): JavaTable =
    databaseManager.createTable(request.tableName, request.columns.asScala.map(_.toScala).toList, request.key, databaseName).toJava


  @RequestMapping(value = Array("/{databaseName}/table/{tableName}"), method = Array(RequestMethod.GET))
  @ResponseBody def findTable(@PathVariable tableName: String, @PathVariable databaseName: String): JavaTable =
    databaseManager.findTable(tableName, databaseName).get.toJava


  @RequestMapping(value = Array("/{databaseName}"), method = Array(RequestMethod.GET))
  @ResponseBody def viewDatabase(@PathVariable databaseName: String): JavaDatabase =
    databaseManager.viewAllTables(databaseName).toJava


  @RequestMapping(value = Array("/"), method = Array(RequestMethod.POST))
  @ResponseBody def createDatabase(@RequestBody databaseName: String): JavaDatabase =
    databaseManager.createDatabase(databaseName).toJava


  @RequestMapping(value = Array("/{databaseName}/table/{tableName}/row"), method = Array(RequestMethod.POST))
  @ResponseBody def insertRow(@RequestBody columnsWithValues: List[JavaColumnWithValue], @PathVariable databaseName: String, @PathVariable tableName: String): Row =
    databaseManager.addRowToTable(
      columnsWithValues.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName)) -> columnWithValue.value),
      tableName,
      databaseName).get


  @RequestMapping(value = Array("/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def deleteRow(@PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): Unit =
    databaseManager.removeRow(keyValue, tableName, databaseName).get

  @RequestMapping(value = Array("/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.POST))
  @ResponseBody def editRow(@RequestBody columnsWithValues: List[JavaColumnWithValue], @PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): Unit =
    databaseManager.editRow(
      columnsWithValues.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName) -> columnWithValue.value)), tableName, databaseName)

  @RequestMapping(value = Array("/{databaseName}/table/{tableName}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def deleteTable(@PathVariable databaseName: String, @PathVariable tableName: String): Unit = databaseManager.dropTable(tableName, databaseName)

  @RequestMapping(value = Array("/{databaseName}/table/{firstTableName}/merge/{secondTableName}"), method = Array(RequestMethod.GET))
  @ResponseBody def mergeTables(@RequestParam joinOn: String, @PathVariable databaseName: String, @PathVariable firstTableName: String, @PathVariable secondTableName: String): Table =
    databaseManager.mergeTables(firstTableName, secondTableName, databaseName, joinOn).get

}

case class JavaRow(@BeanProperty values: java.util.List[String])

case class JavaTable(@BeanProperty tableName: String, @BeanProperty key: String, @BeanProperty rows: java.util.List[JavaRow] = List[JavaRow]().asJava, @BeanProperty columns: java.util.List[JavaColumn])

case class JavaDatabase(@BeanProperty databaseName: String, @BeanProperty tables: java.util.List[JavaTable])

case class JavaColumn(@BeanProperty columnName: String, @BeanProperty columnType: String)

case class JavaColumnWithValue(@BeanProperty columnName: String, @BeanProperty columnType: String, @BeanProperty value: String) {
  def this() = this("", "", "")
}

@JsonIgnoreProperties(ignoreUnknown = true)
case class CreateTableRequest(@BeanProperty var tableName: String, @BeanProperty var key: String, @BeanProperty var columns: java.util.List[JavaColumn]) {

  def this() = this(null, null, null)
}

object Mappers {

  implicit class `Row--->JavaRow`(val row: Row) extends AnyVal {
    def toJava: JavaRow = JavaRow(
      row.values.asJava
    )
  }

  implicit class `Table--->JavaTable`(val table: Table) extends AnyVal {
    def toJava: JavaTable = JavaTable(
      table.name,
      table.key,
      table.rows.map(_.toJava).asJava,
      table.columns.map(_.toJava).asJava
    )
  }

  implicit class `Database--->JavaDatabase`(val database: Database) extends AnyVal {
    def toJava: JavaDatabase = JavaDatabase(
      database.name,
      database.tables.map(_.toJava).asJava
    )
  }

  implicit class `Column--->JavaColumn`(val column: Column) extends AnyVal {
    def toJava: JavaColumn = JavaColumn(
      column.columnName,
      Type.toName(column.columnType)
    )
  }

  implicit class `JavaColumn--->Column`(val column: JavaColumn) extends AnyVal {
    def toScala: Column = Column(
      Type.toType(column.columnType),
      column.columnName
    )
  }

}
