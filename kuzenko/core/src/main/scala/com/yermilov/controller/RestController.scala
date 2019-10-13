package com.yermilov.controller

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.yermilov.domain._
import com.yermilov.manager.{DatabaseManager, OutputManager}
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

import scala.beans.BeanProperty
import scala.collection.JavaConverters._
import org.springframework.web.bind.annotation.{RestController => SpringRestController, _}
import scala.util.{Success, Try}

@SpringRestController
class RestController(override val databaseManager: DatabaseManager, outputManager: OutputManager) extends HateoasSupport {

  import Mappers._

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/{databaseName}/table"), method = Array(RequestMethod.POST))
  @ResponseBody def createTable(@RequestBody request: CreateTableRequest, @PathVariable databaseName: String): JavaTable =
    databaseManager.createTable(request.tableName, request.columns.asScala.map(_.toScala).toList, request.key, databaseName).toJava

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/{databaseName}/table/{tableName}"), method = Array(RequestMethod.GET))
  @ResponseBody def findTable(@PathVariable tableName: String, @PathVariable databaseName: String): JavaTable =
    databaseManager.findTable(tableName, databaseName).get.toJava

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/{databaseName}"), method = Array(RequestMethod.GET))
  @ResponseBody def viewDatabase(@PathVariable databaseName: String): JavaDatabase =
    databaseManager.viewAllTables(databaseName).toJava

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/"), method = Array(RequestMethod.POST))
  @ResponseBody def createDatabase(@RequestBody databaseName: String): JavaDatabase =
    databaseManager.createDatabase(databaseName).toJava

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/{databaseName}/table/{tableName}/row"), method = Array(RequestMethod.POST))
  @ResponseBody def insertRow(@RequestBody columnsWithValues: java.util.List[JavaColumnWithValue], @PathVariable databaseName: String, @PathVariable tableName: String): Row =
    databaseManager.addRowToTable(
      columnsWithValues.asScala.toList.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName)) -> columnWithValue.value),
      tableName,
      databaseName).get

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def deleteRow(@PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): Unit =
    databaseManager.removeRow(keyValue, tableName, databaseName).get

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.POST))
  @ResponseBody def editRow(@RequestBody columnsWithValues: java.util.List[JavaColumnWithValue], @PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): Row =
    databaseManager.editRow(
      columnsWithValues.asScala.toList.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName) -> columnWithValue.value)), tableName, databaseName).get

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/{databaseName}/table/{tableName}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def deleteTable(@PathVariable databaseName: String, @PathVariable tableName: String): Try[Boolean] = Success(databaseManager.dropTable(tableName, databaseName))

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/{databaseName}/table/{firstTableName}/merge/{secondTableName}"), method = Array(RequestMethod.GET))
  @ResponseBody def mergeTables(@RequestParam joinOn: String, @PathVariable databaseName: String, @PathVariable firstTableName: String, @PathVariable secondTableName: String): Table =
    databaseManager.mergeTables(firstTableName, secondTableName, databaseName, joinOn).get

  @CrossOrigin(origins = Array("http://localhost:63342"))
  @RequestMapping(value = Array("/"), method = Array(RequestMethod.GET))
  @ResponseBody def getAllDatabases: List[JavaDatabase] =
    databaseManager.getAllDatabases.map(_.toJava)
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
