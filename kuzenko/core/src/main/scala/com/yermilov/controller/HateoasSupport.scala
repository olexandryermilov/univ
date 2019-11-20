package com.yermilov.controller

import com.yermilov.domain.{Column, Type}
import com.yermilov.manager.DatabaseManager
import org.springframework.hateoas.ResourceSupport
import org.springframework.http.{HttpEntity, HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{PathVariable, RequestBody, RequestMapping, RequestMethod, RequestParam, ResponseBody}
import scala.collection.JavaConverters._
import org.springframework.hateoas.mvc.ControllerLinkBuilder._
import scala.beans.BeanProperty

trait HateoasSupport {
  def databaseManager: DatabaseManager
  import com.yermilov.utils.Mappers._
  @RequestMapping(value = Array("/hateoas/{databaseName}/table"), method = Array(RequestMethod.POST))
  @ResponseBody def hateoasCreateTable(@RequestBody request: CreateTableRequest, @PathVariable databaseName: String): HttpEntity[HateoasJavaTable] = {
    val javaTable = HateoasJavaTable(databaseManager.createTable(request.tableName, request.columns.asScala.map(_.toScala).toList, request.key, databaseName).toJava)
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasCreateTable(request, databaseName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasFindTable(request.tableName, databaseName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasInsertRow(null, databaseName, request.tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasDeleteRow(null, databaseName, request.tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasEditRow(null, null, databaseName, request.tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasViewDatabase(databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaTable](javaTable, HttpStatus.OK)
  }

  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}"), method = Array(RequestMethod.GET))
  @ResponseBody def hateoasFindTable(@PathVariable tableName: String, @PathVariable databaseName: String): HttpEntity[HateoasJavaTable] = {
    val hateoasJavaTable = HateoasJavaTable(databaseManager.findTable(tableName, databaseName).get.toJava)
    hateoasJavaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasDeleteRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasEditRow(null, null, databaseName, tableName)).withSelfRel())
    hateoasJavaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasViewDatabase(databaseName)).withSelfRel())
    new ResponseEntity(hateoasJavaTable, HttpStatus.OK)
  }


  @RequestMapping(value = Array("/hateoas/{databaseName}"), method = Array(RequestMethod.GET))
  @ResponseBody def hateoasViewDatabase(@PathVariable databaseName: String): HttpEntity[HateoasJavaDatabase] = {
    val hateoasJavaDatabase = HateoasJavaDatabase(databaseManager.viewAllTables(databaseName).toJava)
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasCreateTable(null, databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasFindTable(null, databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasDeleteTable(databaseName, null)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasViewDatabase(databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaDatabase](hateoasJavaDatabase, HttpStatus.OK)
  }


  @RequestMapping(value = Array("/hateoas/"), method = Array(RequestMethod.POST))
  @ResponseBody def hateoasCreateDatabase(@RequestBody databaseName: String): HttpEntity[HateoasJavaDatabase] = {
    val hateoasJavaDatabase = HateoasJavaDatabase(databaseManager.createDatabase(databaseName).toJava)
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasCreateTable(null, databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasFindTable(null, databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasDeleteTable(databaseName, null)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasViewDatabase(databaseName)).withSelfRel())
    hateoasJavaDatabase.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasCreateDatabase(databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaDatabase](hateoasJavaDatabase, HttpStatus.OK)
  }


  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}/row"), method = Array(RequestMethod.POST))
  @ResponseBody def hateoasInsertRow(@RequestBody columnsWithValues: java.util.List[JavaColumnWithValue], @PathVariable databaseName: String, @PathVariable tableName: String): HttpEntity[HateoasJavaRow] = {
    val hateoasJavaRow = HateoasJavaRow(databaseManager.addRowToTable(
      columnsWithValues.asScala.toList.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName)) -> columnWithValue.value),
      tableName,
      databaseName).get.toJava)
    hateoasJavaRow.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasEditRow(null, null, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasDeleteRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasFindTable(tableName, databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaRow](hateoasJavaRow, HttpStatus.OK)
  }

  case class HateoasDeleteResponse(@BeanProperty val content: Boolean) extends ResourceSupport

  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def hateoasDeleteRow(@PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): HttpEntity[HateoasDeleteResponse] = {
    val response = HateoasDeleteResponse(databaseManager.removeRow(keyValue, tableName, databaseName, "key").isSuccess)
    response.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasFindTable(tableName, databaseName)).withSelfRel())
    response.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    new ResponseEntity[HateoasDeleteResponse](response, HttpStatus.OK)
  }

  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}/row/{keyValue}"), method = Array(RequestMethod.POST))
  @ResponseBody def hateoasEditRow(@RequestBody columnsWithValues: java.util.List[JavaColumnWithValue], @PathVariable keyValue: String, @PathVariable databaseName: String, @PathVariable tableName: String): HttpEntity[HateoasJavaRow] = {
    val hateoasJavaRow = HateoasJavaRow(databaseManager.editRow(
      columnsWithValues.asScala.toList.map(columnWithValue => (Column(Type.toType(columnWithValue.columnType), columnWithValue.columnName) -> columnWithValue.value)), tableName, databaseName).get.toJava)
    hateoasJavaRow.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasEditRow(null, keyValue, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasDeleteRow(keyValue, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    hateoasJavaRow.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasFindTable(tableName, databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaRow](hateoasJavaRow, HttpStatus.OK)
  }

  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{tableName}"), method = Array(RequestMethod.DELETE))
  @ResponseBody def hateoasDeleteTable(@PathVariable databaseName: String, @PathVariable tableName: String): HttpEntity[HateoasDeleteResponse] = {
    val response = HateoasDeleteResponse(databaseManager.dropTable(tableName, databaseName))
    response.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasViewDatabase(databaseName)).withSelfRel())
    response.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasCreateTable(null, databaseName)).withSelfRel())
    new ResponseEntity[HateoasDeleteResponse](response, HttpStatus.OK)
  }


  @RequestMapping(value = Array("/hateoas/{databaseName}/table/{firstTableName}/merge/{secondTableName}"), method = Array(RequestMethod.GET))
  @ResponseBody def hateoasMergeTables(@RequestParam joinOn: String, @PathVariable databaseName: String, @PathVariable firstTableName: String, @PathVariable secondTableName: String): HttpEntity[HateoasJavaTable] = {
    val javaTable = HateoasJavaTable(databaseManager.mergeTables(firstTableName, secondTableName, databaseName, joinOn).get.toJava)
    val tableName = javaTable.content.getName
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasFindTable(tableName, databaseName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasInsertRow(null, databaseName, tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasDeleteRow(null, databaseName, tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasEditRow(null, null, databaseName, tableName)).withSelfRel())
    javaTable.add(linkTo(methodOn(classOf[HateoasSupport]).hateoasViewDatabase(databaseName)).withSelfRel())
    new ResponseEntity[HateoasJavaTable](javaTable, HttpStatus.OK)
  }

}

case class HateoasJavaRow(@BeanProperty val content: JavaRow) extends ResourceSupport

case class HateoasJavaTable(@BeanProperty val content: JavaTable) extends ResourceSupport

case class HateoasJavaDatabase(@BeanProperty val content: JavaDatabase) extends ResourceSupport
