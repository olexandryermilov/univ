package com.yermilov.graphql

import com.yermilov.domain.{Database, Row, Table}
import com.yermilov.manager.DatabaseManager

class DatabaseService(databaseManager: DatabaseManager) {

  def getDatabase(databaseName: String): Database =
    databaseManager.viewAllTables(databaseName)

  def getTable(databaseName: String, tableName: String): Table = databaseManager.findTable(tableName, databaseName).get

  def getRow(databaseName: String, tableName: String, keyValue: String): Option[Row] = {
    val table = databaseManager.findTable(tableName, databaseName).get
    val keyColumnIndex = table.columns.map(_.columnName).indexOf(table.key)
    val row = table.rows.filter(row => row.values(keyColumnIndex) == keyValue)
    row.headOption
  }

  def mergeTables(databaseName: String, firstTableName: String, secondTableName: String, mergeOn: String): Table = {
    databaseManager.mergeTables(firstTableName, secondTableName, databaseName, mergeOn).get
  }

}

