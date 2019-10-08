package com.yermilov.commands

import com.yermilov.manager.{DatabaseManager, OutputManager}

trait Command {
  def run(parameters: Map[String, Any], databaseManager: DatabaseManager, outputManager: OutputManager)
}
