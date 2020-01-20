package com.yermilov.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.yermilov.graphql.{DatabaseQuery, DatabaseService}
import com.yermilov.manager.{DatabaseManager, FileSystemDatabaseManager}
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{Bean, Configuration}

object MainApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[MainApplication], args: _ *)
  }
}

@SpringBootApplication
class MainApplication {

  @Bean
  def databaseManager: DatabaseManager = new FileSystemDatabaseManager()

  @Bean
  def databaseService(databaseManager: DatabaseManager): DatabaseService = new DatabaseService(databaseManager)

  @Bean
  def databaseQuery(databaseService: DatabaseService): DatabaseQuery = new DatabaseQuery(databaseService)
}

@Configuration
class JacksonConfiguration {

  @Bean
  def objectMapper: ObjectMapper = {
    val objectMapper = new ObjectMapper()
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper
  }
}
