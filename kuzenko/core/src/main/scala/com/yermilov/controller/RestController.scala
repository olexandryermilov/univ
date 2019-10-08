package com.yermilov.controller

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.yermilov.domain._
import com.yermilov.manager.{DatabaseManager, OutputManager}
import org.springframework.hateoas.ResourceSupport
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

import scala.beans.BeanProperty
import scala.collection.JavaConverters._
import org.springframework.web.bind.annotation.{RestController => SpringRestController, _}
import org.springframework.hateoas.mvc.ControllerLinkBuilder._
import org.springframework.http.{HttpEntity, HttpStatus, ResponseEntity}

import scala.util.{Success, Try}

@SpringRestController
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
  @ResponseBody def insertRow(@RequestBody columnsWithValues: java.util.List[JavaColumnWithValue], @PathVariable databaseName: String, @PathVariable tableName: String): Row =
    databaseManager.addRowToTable(
      columnsWithValues.asScala.toList.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName)) -> columnWithValue.value),
      tableName,
      databaseName).get


  @RequestMapping(value = Array("/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def deleteRow(@PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): Unit =
    databaseManager.removeRow(keyValue, tableName, databaseName).get

  @RequestMapping(value = Array("/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.POST))
  @ResponseBody def editRow(@RequestBody columnsWithValues: java.util.List[JavaColumnWithValue], @PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): Row =
    databaseManager.editRow(
      columnsWithValues.asScala.toList.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName) -> columnWithValue.value)), tableName, databaseName).get

  @RequestMapping(value = Array("/{databaseName}/table/{tableName}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def deleteTable(@PathVariable databaseName: String, @PathVariable tableName: String): Try[Boolean] = Success(databaseManager.dropTable(tableName, databaseName))

  @RequestMapping(value = Array("/{databaseName}/table/{firstTableName}/merge/{secondTableName}"), method = Array(RequestMethod.GET))
  @ResponseBody def mergeTables(@RequestParam joinOn: String, @PathVariable databaseName: String, @PathVariable firstTableName: String, @PathVariable secondTableName: String): Table =
    databaseManager.mergeTables(firstTableName, secondTableName, databaseName, joinOn).get

  @RequestMapping(value = Array("/hateoas/{databaseName}/table"), method = Array(RequestMethod.POST))
  @ResponseBody def hateoasCreateTable(@RequestBody request: CreateTableRequest, @PathVariable databaseName: String): HttpEntity[HateoasJavaTable] = {
    val javaTable = HateoasJavaTable(databaseManager.createTable(request.tableName, request.columns.asScala.map(_.toScala).toList, request.key, databaseName).toJava)
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasCreateTable(request, databaseName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasFindTable(request.tableName, databaseName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasInsertRow(null, databaseName, request.tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasDeleteRow(null, databaseName, request.tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasEditRow(null, null, databaseName, request.tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasViewDatabase(databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaTable](javaTable, HttpStatus.OK)
  }

  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}"), method = Array(RequestMethod.GET))
  @ResponseBody def hateoasFindTable(@PathVariable tableName: String, @PathVariable databaseName: String): HttpEntity[HateoasJavaTable] = {
    val hateoasJavaTable = HateoasJavaTable(databaseManager.findTable(tableName, databaseName).get.toJava)
    hateoasJavaTable.add(linkTo(methodOn(classOf[RestController]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaTable.add(linkTo(methodOn(classOf[RestController]).hateoasDeleteRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaTable.add(linkTo(methodOn(classOf[RestController]).hateoasEditRow(null, null, databaseName, tableName)).withSelfRel())
    hateoasJavaTable.add(linkTo(methodOn(classOf[RestController]).hateoasViewDatabase(databaseName)).withSelfRel())
    new ResponseEntity(hateoasJavaTable, HttpStatus.OK)
  }


  @RequestMapping(value = Array("/hateoas/{databaseName}"), method = Array(RequestMethod.GET))
  @ResponseBody def hateoasViewDatabase(@PathVariable databaseName: String): HttpEntity[HateoasJavaDatabase] = {
    val hateoasJavaDatabase = HateoasJavaDatabase(databaseManager.viewAllTables(databaseName).toJava)
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[RestController]).hateoasCreateTable(null, databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[RestController]).hateoasFindTable(null, databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[RestController]).hateoasDeleteTable(databaseName, null)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[RestController]).hateoasViewDatabase(databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaDatabase](hateoasJavaDatabase, HttpStatus.OK)
  }


  @RequestMapping(value = Array("/hateoas/"), method = Array(RequestMethod.POST))
  @ResponseBody def hateoasCreateDatabase(@RequestBody databaseName: String): HttpEntity[HateoasJavaDatabase] = {
    val hateoasJavaDatabase = HateoasJavaDatabase(databaseManager.createDatabase(databaseName).toJava)
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[RestController]).hateoasCreateTable(null, databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[RestController]).hateoasFindTable(null, databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[RestController]).hateoasDeleteTable(databaseName, null)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[RestController]).hateoasViewDatabase(databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[RestController]).hateoasCreateDatabase(databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaDatabase](hateoasJavaDatabase, HttpStatus.OK)
  }


  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}/row"), method = Array(RequestMethod.POST))
  @ResponseBody def hateoasInsertRow(@RequestBody columnsWithValues: java.util.List[JavaColumnWithValue], @PathVariable databaseName: String, @PathVariable tableName: String): HttpEntity[HateoasJavaRow] = {
    val hateoasJavaRow = HateoasJavaRow(databaseManager.addRowToTable(
      columnsWithValues.asScala.toList.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName)) -> columnWithValue.value),
      tableName,
      databaseName).get.toJava)
    hateoasJavaRow.add(linkTo(methodOn(classOf[RestController]).hateoasEditRow(null, null, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[RestController]).hateoasDeleteRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[RestController]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[RestController]).hateoasFindTable(tableName, databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaRow](hateoasJavaRow, HttpStatus.OK)
  }

  case class HateoasDeleteResponse(@BeanProperty val content: Boolean) extends ResourceSupport

  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def hateoasDeleteRow(@PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): HttpEntity[HateoasDeleteResponse] = {
    val response = HateoasDeleteResponse(databaseManager.removeRow(keyValue, tableName, databaseName).isSuccess)
    response.add(linkTo(methodOn(classOf[RestController]).hateoasFindTable(tableName, databaseName)).withSelfRel())
    response.add(linkTo(methodOn(classOf[RestController]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    new ResponseEntity[HateoasDeleteResponse](response, HttpStatus.OK)
  }

  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.POST))
  @ResponseBody def hateoasEditRow(@RequestBody columnsWithValues: java.util.List[JavaColumnWithValue], @PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): HttpEntity[HateoasJavaRow] = {
    val hateoasJavaRow = HateoasJavaRow(databaseManager.editRow(
      columnsWithValues.asScala.toList.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName) -> columnWithValue.value)), tableName, databaseName).get.toJava)
    hateoasJavaRow.add(linkTo(methodOn(classOf[RestController]).hateoasEditRow(null, keyValue, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[RestController]).hateoasDeleteRow(keyValue, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[RestController]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[RestController]).hateoasFindTable(tableName, databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaRow](hateoasJavaRow, HttpStatus.OK)
  }

  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def hateoasDeleteTable(@PathVariable databaseName: String, @PathVariable tableName: String): HttpEntity[HateoasDeleteResponse] = {
    val response = HateoasDeleteResponse(databaseManager.dropTable(tableName, databaseName))
    response.add(linkTo(methodOn(classOf[RestController]).hateoasViewDatabase(databaseName)).withSelfRel())
    response.add(linkTo(methodOn(classOf[RestController]).hateoasCreateTable(null, databaseName)).withSelfRel())
    new ResponseEntity[HateoasDeleteResponse](response, HttpStatus.OK)
  }


  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{firstTableName}/merge/{secondTableName}"), method = Array(RequestMethod.GET))
  @ResponseBody def hateoasMergeTables(@RequestParam joinOn: String, @PathVariable databaseName: String, @PathVariable firstTableName: String, @PathVariable secondTableName: String): HttpEntity[HateoasJavaTable] = {
    val javaTable = HateoasJavaTable(databaseManager.mergeTables(firstTableName, secondTableName, databaseName, joinOn).get.toJava)
    val tableName = javaTable.content.tableName
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasFindTable(tableName, databaseName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasDeleteRow(null, databaseName, tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasEditRow(null, null, databaseName, tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[RestController]).hateoasViewDatabase(databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaTable](javaTable, HttpStatus.OK)
  }

}

case class JavaRow(@BeanProperty values: java.util.List[String])

case class JavaTable(@BeanProperty tableName: String, @BeanProperty key: String, @BeanProperty rows: java.util.List[JavaRow] = List[JavaRow]().asJava, @BeanProperty columns: java.util.List[JavaColumn])

case class JavaDatabase(@BeanProperty databaseName: String, @BeanProperty tables: java.util.List[JavaTable])

case class JavaColumn(@BeanProperty columnName: String, @BeanProperty columnType: String)

case class JavaColumnWithValue(@BeanProperty columnName: String, @BeanProperty columnType: String, @BeanProperty value: String) {
  def this() = this("", "", "")
}

case class HateoasJavaRow(@BeanProperty val content: JavaRow) extends ResourceSupport

case class HateoasJavaTable(@BeanProperty val content: JavaTable) extends ResourceSupport

case class HateoasJavaDatabase(@BeanProperty val content: JavaDatabase) extends ResourceSupport

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
