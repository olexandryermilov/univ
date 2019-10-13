package com.yermilov.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.yermilov.controller.RestController
import com.yermilov.manager.{DatabaseManager, OutputManager}
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.servlet.config.annotation.{CorsRegistry, WebMvcConfigurer, WebMvcConfigurerAdapter}

object MainApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[MainApplication], args: _ *)
  }
}

@SpringBootApplication
class MainApplication {

  @Bean
  def databaseManager: DatabaseManager = new DatabaseManager()

  @Bean
  def outputManager: OutputManager = new OutputManager()

  @Bean
  def restController: RestController = new RestController(databaseManager, outputManager)

  @Bean
  def corsConfigurer(): WebMvcConfigurer = {
    new WebMvcConfigurer() {
      override def addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:63342").allowedMethods("GET", "POST", "PUT", "DELETE");
      }
    }
  }
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
