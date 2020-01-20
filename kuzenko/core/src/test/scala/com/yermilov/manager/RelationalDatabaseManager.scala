package com.yermilov.manager

import com.yermilov.domain.{Column, Row, Table, Type}
import com.yermilov.repository.DatabaseRepository
import org.junit.{Before, Test}
import org.mockito.MockitoSugar
import org.mockito.stubbing.ScalaOngoingStubbing

import scala.util.{Success, Try}

class RelationalDatabaseManagerTest extends relctx {

  import org.mockito.MockitoAnnotations

  @Before def init(): Unit = {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  def testDbManager(): Unit = {
    givenTable()
    assert(dbManager.findTable("tabletable", "test") == Success(Table(
     name = "tabletable",
      rows = List.empty,
      columns = List(
      Column(Type.Integer, "id"),
      Column(Type.String, "name")
    ),
      key = "id"
    )))
  }

}

trait relctx extends MockitoSugar {
  val repository = mock[DatabaseRepository]
  val dbManager = new RelationalDatabaseManager(repository)

  def givenTable(databaseName: String = "test", tableName: String = "tabletable", rows: List[Row] = List.empty, key: String ="id", columns: List[Column] = List(
    Column(Type.Integer, "id"),
    Column(Type.String, "name")
  )): ScalaOngoingStubbing[Try[Table]] =
    when(repository.findTable(tableName)) thenReturn Success(
      Table(
        rows = rows,
        name = tableName,
        columns = columns,
        key = key
      )
    )
}
