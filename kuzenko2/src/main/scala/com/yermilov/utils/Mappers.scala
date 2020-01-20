import com.yermilov.controller.{JavaColumn, JavaDatabase, JavaRow, JavaTable}
import com.yermilov.domain.{Column, Database, Row, Table, Type}
import scala.collection.JavaConverters._

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

