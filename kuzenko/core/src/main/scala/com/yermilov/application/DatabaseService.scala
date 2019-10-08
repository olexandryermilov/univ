package com.yermilov.application

import com.yermilov.domain.Database
import com.yermilov.manager.DatabaseManager
import org.springframework.stereotype.Service

@Service
class DatabaseService(databaseManager: DatabaseManager) {

  def getDatabase(databaseName: String): Database = {
    databaseManager.viewAllTables(databaseName)
  }
}
