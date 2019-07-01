package commands

import manager.{DatabaseManager, OutputManager}

trait Command {
  def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager)
}
