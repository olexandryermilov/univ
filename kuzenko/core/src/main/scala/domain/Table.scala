package domain

case class Table(rows: List[Row], name: String, columns: List[Column], key: String)
