package com.yermilov.manager

import com.yermilov.controller.JavaDatabase
import com.yermilov.domain.{Column, Database, Row, Table}
import com.yermilov.repository.DatabaseRepository

import scala.util.Try

class RelationalDatabaseManager(databaseRepository: DatabaseRepository) extends DatabaseManager {
  override def createTable(tableName: String, columns: List[Column], key: String, databaseName: String): Table = {
    databaseRepository.createTable(databaseName, tableName, columns, key)
    Table(List.empty, tableName, columns, key)
  }

  override def addRowToTable(columnsWithValues: List[(Column, String)],
                             tableName: String,
                             databaseName: String): Try[Row] = databaseRepository.insertRow(tableName, columnsWithValues)

  override def dropTable(tableName: String, databaseName: String): Boolean = databaseRepository.dropTable(tableName).isSuccess

  override def findTable(tableName: String, databaseName: String): Try[Table] = databaseRepository.findTable(tableName)

  override def removeRow(value: String, tableName: String, databaseName: String, key: String): Try[Unit] = databaseRepository.removeRow(value, tableName, key)

  override def editRow(columnsAndValues: List[(Column, String)], tableName: String, databaseName: String): Try[Row] = ???

  override def viewAllTables(databaseName: String): Database = databaseRepository.viewAllTables()

  override def viewAllTablesJava(databaseName: String): JavaDatabase = ???

  override def mergeTables(tableName1: String, tableName2: String, databaseName: String, joinOn: String): Try[Table] =
    databaseRepository.merge(tableName1, tableName2, joinOn)

  override def createDatabase(databaseName: String): Database = ???

  override def getAllDatabases: List[Database] = List(viewAllTables("test"))

  override def deleteDatabase(databaseName: String): Unit = ???
}
