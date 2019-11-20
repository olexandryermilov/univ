package com.yermilov.repository

import java.sql.ResultSet

import com.yermilov.domain.Type.{CharInv, Real, Type}
import com.yermilov.domain.{Column, Database, Row, Table, Type}
import org.springframework.jdbc.core.{JdbcTemplate, RowMapper}

import collection.JavaConverters._
import scala.collection.immutable
import scala.util.Try

class DatabaseRepository(jdbcTemplate: JdbcTemplate) {
  def createDatabase() = ???

  def viewAllTables(): Database = {
    val q = "select table_type, table_name FROM INFORMATION_SCHEMA.TABLES where table_type = 'TABLE'"
    val res = jdbcTemplate.query(q, tableNameRowMapper).asScala.toList
    Database(res.map(findTable).map(_.get), "test")
  }

  private def tableNameRowMapper = new RowMapper[String] {
    override def mapRow(rs: ResultSet, rowNum: Int): String = rs.getString("table_name")
  }


  def createTable(databaseName: String, tableName: String, columns: List[Column], key: String): Unit = Try {
    val q =
      s"""
         |CREATE TABLE $tableName (
         |${columns.map(columnToSql).mkString(",\n")}
         |);
         |""".stripMargin
    val r = jdbcTemplate.execute(q)
    println("ok $q")
    r
  }.get

  def dropTable(tableName: String): Try[Unit] = Try {
    val q =
      s"""
         |DROP TABLE $tableName ;
         |""".stripMargin
    jdbcTemplate.execute(q)
  }

  def findTable(tableName: String): Try[Table] = Try {
    val q =
      s"""
         |SELECT * FROM $tableName;
         |""".stripMargin
    val table = jdbcTemplate.queryForList(q).asScala.toList.map(_.asScala)
    val columnsQuery = s"SELECT column_name, column_type FROM INFORMATION_SCHEMA.COLUMNS where table_schema = 'PUBLIC' and TABLE_NAME = '$tableName'"
    val columns = jdbcTemplate.query(columnsQuery, columnsRowMapper).asScala.toList
    Table(
      rows = table.map(_.values).map(r => Row(r.toList.map(v => v.toString))),
      name = tableName,
      columns = columns,
      key = columns.headOption.map(_.columnName).getOrElse("")
    )
  }

  def removeRow(value: String, tableName: String, key: String): Try[Unit] = Try {
    println(s"!!!!!!$key")
    val q = s"DELETE FROM $tableName where $key = $value"
    jdbcTemplate.execute(q)
  }

  def insertRow(tableName: String, columnWithValues: List[(Column, String)]): Try[Row] = Try {
    val columns = columnWithValues.map(_._1.columnName).mkString(",")
    val values = columnWithValues.map(cv => {
      val needWrapping = cv._1.columnType match {
        case Type.Integer | Type.Real => false
        case _ => true
      }
      if (needWrapping) s"'${cv._2}'" else cv._2
    }).mkString(",")
    val q =
      s"""
         |INSERT INTO $tableName ($columns)
         |values ($values);
         |""".stripMargin

    jdbcTemplate.execute(q)
    Row(columnWithValues.map(_._2))
  }

  def merge(tableName1: String, tableName2: String, joinOn: String): Table = {
    val q =
      s"""
        |select * from $tableName1 join $tableName2 on $tableName1.$joinOn = $tableName2.$joinOn
        |""".stripMargin
    val table = jdbcTemplate.queryForList(q).asScala.toList.map(_.asScala)
    val firstColumnsQuery = s"SELECT column_name, column_type FROM INFORMATION_SCHEMA.COLUMNS where table_schema = 'PUBLIC' and TABLE_NAME = '$tableName1'"
    val firstColumns = jdbcTemplate.query(firstColumnsQuery, columnsRowMapper).asScala.toSet
    val secondColumnsQuery = s"SELECT column_name, column_type FROM INFORMATION_SCHEMA.COLUMNS where table_schema = 'PUBLIC' and TABLE_NAME = '$tableName2'"
    val secondColumns = jdbcTemplate.query(secondColumnsQuery, columnsRowMapper).asScala.toSet
    val columns = (firstColumns ++ secondColumns).toList
    val result = Table(
      rows = table.map(_.values).map(r => Row(r.toList.map(v => v.toString))),
      name = s"${tableName1}_merge_${tableName2}",
      columns = columns,
      key = columns.headOption.map(_.columnName).getOrElse("")
    )
    createTable("", result.name, columns, joinOn)
    val columnsWithValues = result.rows.map(_.values.zip(columns)).map(c => c.map(x=>(x._2 -> x._1)))
    columnsWithValues.foreach(
      insertRow(result.name, _)
    )
    result
  }

  val columnsRowMapper = new RowMapper[Column] {
    override def mapRow(rs: ResultSet, rowNum: Int): Column = {
      val columnName = rs.getString("column_name")
      val columnType = rs.getString("column_type")
      Column(sqlToColumnType(columnType), columnName)
    }
  }

  private def columnToSql(column: Column): String = {
    val sqlType = column.columnType match {
      case Type.Integer => "int"
      case Real => "real"
      case Type.Char => "char"
      case Type.String => "varchar"
      case CharInv => "varchar"
    }
    s"${column.columnName} ${sqlType}"
  }

  private def sqlToColumnType(sqlType: String): Type = sqlType.toLowerCase match {
    case "int" => Type.Integer
    case "real" => Type.Real
    case "char" => Type.Char
    case "varchar" => Type.String
    case _ => Type.String
  }

}
