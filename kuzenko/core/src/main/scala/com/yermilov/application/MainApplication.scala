package com.yermilov.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.yermilov.controller.RestController
import com.yermilov.graphql.{DatabaseQuery, DatabaseService}
import com.yermilov.manager.{DatabaseManager, FileSystemDatabaseManager, OutputManager, RelationalDatabaseManager}
import com.yermilov.repository.DatabaseRepository
import javax.sql.DataSource
//import org.mongodb.scala.MongoClient
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{EnableAutoConfiguration, SpringBootApplication}
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.servlet.config.annotation.{CorsRegistry, WebMvcConfigurer, WebMvcConfigurerAdapter}

object MainApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[MainApplication], args: _ *)
  }
}

/*mongodb+srv://oleksandr:kuzenko@kuzenko-nepyp.gcp.mongodb.net/test?retryWrites=true&w=majority*/

@SpringBootApplication
class MainApplication {

  @Bean
  def databaseRepository(): DatabaseRepository = {
    val dataSourceBuilder = DataSourceBuilder.create()
    dataSourceBuilder.driverClassName("org.h2.Driver")
    dataSourceBuilder.url("jdbc:h2:mem:testdb")
    dataSourceBuilder.username("SA")
    dataSourceBuilder.password("password")
    new DatabaseRepository(new JdbcTemplate(dataSourceBuilder.build().asInstanceOf[DataSource]))
  }

  /*
  @Bean
  def databaseManager(databaseRepository: DatabaseRepository): DatabaseManager = new RelationalDatabaseManager(databaseRepository)
   */

  @Bean
  def databaseManager(): DatabaseManager = new FileSystemDatabaseManager()

  /*@Bean
  def databaseManager(objectMapper: ObjectMapper): DatabaseManager = {
    System.setProperty("org.mongodb.async.type", "netty")
    val mongoClient: MongoClient = MongoClient("mongodb+srv://oleksandr:kuzenko@kuzenko-nepyp.gcp.mongodb.net/test?retryWrites=true&w=majority")
    new MongoDatabaseManager(
      jsonMapper = objectMapper,
      mongoClient = mongoClient
    )
  }*/

  @Bean
  def outputManager: OutputManager = new OutputManager()

  @Bean
  def databaseService(databaseManager: DatabaseManager): DatabaseService = new DatabaseService(databaseManager)

  @Bean
  def databaseQuery(databaseService: DatabaseService): DatabaseQuery = new DatabaseQuery(databaseService)

  @Bean
  def restController(databaseManager: DatabaseManager, outputManager: OutputManager): RestController = new RestController(databaseManager, outputManager)

  /*
  @Bean
  def corsConfigurer(): WebMvcConfigurer = {
    new WebMvcConfigurer() {
      override def addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:63342", "http://localhost:3000").allowedMethods("GET", "POST", "PUT", "DELETE");
      }
    }
  }
  */
   */
}

@Configuration
class JacksonConfiguration {

  @Bean
  def objectMapper: ObjectMapper = {
    val objectMapper = new ObjectMapper() //with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper
  }
}
