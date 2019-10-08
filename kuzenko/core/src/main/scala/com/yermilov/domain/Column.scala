package com.yermilov.domain

import com.yermilov.domain.Type.Type

import scala.beans.BeanProperty

case class Column(@BeanProperty columnType: Type, @BeanProperty columnName: String)
