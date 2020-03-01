import cats.instances.future._
import cats.syntax.functor._
import com.bot4s.telegram.api.RequestHandler
import com.bot4s.telegram.clients.ScalajHttpClient
import com.bot4s.telegram.future.{Polling, TelegramBot}
import com.bot4s.telegram.methods._
import com.bot4s.telegram.models._
import slogging.{LogLevel, LoggerConfig, PrintLoggerFactory}

import scala.concurrent.Future

class ElizaBot(token: String) extends TelegramBot
  with Polling {
  LoggerConfig.factory = PrintLoggerFactory()
  // set log level, e.g. to TRACE
  LoggerConfig.level = LogLevel.ERROR

  override val client: RequestHandler[Future] = new ScalajHttpClient(token)
  override def receiveMessage(msg: Message): Future[Unit] =
  if(msg.from.get.username.contains("olexandryermilov") || msg.from.get.username.map(_.toLowerCase).contains("marchaprilmay") || msg.from.get.lastName.get == "Yermilov") msg.text.fold(Future.successful(())) { text => {
    println(s"${msg.from.get.username.get}: $text")
    val response = Eliza.prepareResponse(text)
    println(s"ELIZA: $text")
    request(SendMessage(msg.source, response)).void
  }
  }
  else Future.successful(())
}
