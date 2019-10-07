package domain

import scala.beans.BeanProperty

case class Table(@BeanProperty rows: List[Row], @BeanProperty name: String, @BeanProperty columns: List[Column], @BeanProperty key: String)
