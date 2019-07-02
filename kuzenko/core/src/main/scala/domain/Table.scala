package domain

case class Table(rows: Seq[Row], name: String, columnType: Seq[Column], key: String)
