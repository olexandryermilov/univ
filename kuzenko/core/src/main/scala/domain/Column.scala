package domain

import domain.Type.Type

import scala.beans.BeanProperty

case class Column(@BeanProperty columnType: Type, @BeanProperty columnName: String)
