package com.yermilov.graphql

//import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.yermilov.controller.{JavaDatabase, JavaRow, JavaTable}

class DatabaseQuery(databaseService: DatabaseService) /*extends GraphQLQueryResolver {

  import com.yermilov.utils.Mappers._

  def getDatabase(databaseName: String): JavaDatabase =
    databaseService.getDatabase(databaseName).toJava

  def getTable(databaseName: String, tableName: String): JavaTable =
    databaseService.getTable(databaseName, tableName).toJava

  def getRow(databaseName: String, tableName: String, keyValue: String): JavaRow = databaseService.getRow(databaseName, tableName, keyValue).map(_.toJava).getOrElse(throw new RuntimeException("No row with such key column"))

  def mergeTables(databaseName: String, firstTableName: String, secondTableName: String, mergeOn: String): JavaTable =
    databaseService.mergeTables(databaseName: String, firstTableName: String, secondTableName: String, mergeOn: String).toJava

}
*/
