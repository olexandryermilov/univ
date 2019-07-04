package domain

case class Table(rows: Seq[Row], name: String, columns: Seq[Column], key: String)
