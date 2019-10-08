package com.yermilov.controller

import com.yermilov.domain.{Column, Row, Table, Type}
import com.yermilov.manager.{DatabaseManager, OutputManager}
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ControllerLinkBuilder._
import org.springframework.http.{HttpEntity, HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{RestController => SpringRestController, _}

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

@SpringRestController
class RestHATEOASController(databaseManager: DatabaseManager, outputManager: OutputManager) {

  import Mappers._


}
