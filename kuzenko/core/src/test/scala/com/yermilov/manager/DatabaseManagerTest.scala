package com.yermilov.manager

import com.yermilov.domain.Type.{Char, CharInv, Integer, Real, String}
import com.yermilov.domain.{Column, Row, Table}
import org.junit.Test
import com.yermilov.utils.Parameters.{dbLocation, tableExtension}
import com.yermilov.utils.{DBFileUtils, ScalaFileUtils}

import scala.util.Random

class DatabaseManagerTest extends ctx {

  /*@Test
  def createTable_createdTableAndWriteItToFile {
    databaseManager.createTable(
      tableName,
      columns.reverse,
      key,
      databaseName
    )
    assert(ScalaFileUtils.readFile(s"$dbLocation$databaseName/$tableName$tableExtension") == (
      List(
        "class Char,group CharInv,keyColumn Integer,money Real,name String"
      )
      ), "Table created and written to file")
  }

  /*@Test
  def createTable_andValidateThatKeyExits: Unit ={

  }*/

  @Test
  def mergeTable: Unit = {
    val dbFileUtils = new DBFileUtils
    val someTable = table.copy(rows = List(generateRowForFirstTable, generateRowForFirstTable, generateRowForFirstTable))
    dbFileUtils.saveTableTo(s"$databaseName/", someTable)
    dbFileUtils.saveTableTo(s"$databaseName/", anotherTable)

    val mergedTable = Table(
      someTable.rows.map( r => Row(fillRowsUntil(7, r))),
      s"${tableName}_merge_$anotherTableName",
      columns ++ anotherColumns.dropRight(1),
      "name"
    )

    val result = databaseManager.mergeTables(tableName, anotherTableName, databaseName, "name").get

    assert(result == mergedTable)
  }*/
  /*def mergeTable(): Unit = {

    when(dbFileUtils.readTable(s"$databaseName/", tableName)).thenReturn(
      Try(table.copy(rows = List(generateRowForFirstTable, generateRowForFirstTable, generateRowForFirstTable)))
    )

    when(dbFileUtils.readTable(s"$databaseName/", anotherTableName)).thenReturn(
      Try(anotherTable)
    )

    databaseManagerWithMocks.mergeTables(
      tableName, anotherTableName, databaseName, "name"
    )

    val mergedTable = Table(
      table.rows.map( r => Row(fillRowsUntil(7, r))) ++ anotherTable.rows.map(fillSecondRows),
      s"${tableName}_merged_$anotherTableName",
      columns ++ anotherColumns,
      "name"
    )

    verify(dbFileUtils).saveTableTo(s"$databaseName", mergedTable)
  }*/

}

trait ctx {
  val key = "keyColumn"
  val tableName = "TABLE"
  val columns = List(
    Column(Char, "class"),
    Column(CharInv, "group"),
    Column(Integer, "keyColumn"),
    Column(Real, "money"),
    Column(String, "name")
  )

  val table = Table(List.empty, tableName, columns, key)

  def generateRowForFirstTable: Row = Row(List[String](
    Random.nextPrintableChar().toString,
    Random.nextString(2).sortWith((a, b) => a.compareTo(b) < 0).toString,
    Random.nextInt.toString,
    Random.nextDouble.toString,
    randomString
  ))

  def generateRowForSecondTable: Row = Row(List(
    Random.nextPrintableChar().toString,
    Random.nextString(2).sortWith((a, b) => a.compareTo(b) < 0).toString,
    randomString
  ))

  def fillRowsUntil(n: Int, r: Row): List[String] = {
    var resultValues = r.values
    while(resultValues.size < n) resultValues ++= List("")
    resultValues
  }

  def fillSecondRows(r: Row): Row = {
    r.copy(values = List("", "", "", "")++ r.values)
  }

  def randomString: String = Random.nextString(Random.nextInt(10))

  val anotherColumns = List(
    Column(Char, "ycolumn1"),
    Column(CharInv, "zcolumn2"),
    Column(String, "name")
  )
  val anotherTableName = "anotherTable"
  val anotherTable = Table(
    List(generateRowForSecondTable, generateRowForSecondTable, generateRowForSecondTable),
    anotherTableName,
    anotherColumns,
    "name"
  )
  val databaseName = "databaseName"
  //val valueValidator: ValueValidator = mock[ValueValidator]
  //val dbFileUtils: DBFileUtils = mock[DBFileUtils]
  //val databaseManagerWithMocks = new DatabaseManager(valueValidator, dbFileUtils)
  val databaseManager = new FileSystemDatabaseManager()
}
