package com.dalcim.testworkmanager.database

import com.dalcim.testworkmanager.domain.LogEntity
import com.google.gson.Gson

class LogDatabase {

    private val fileManager by lazy { FileManager() }
    private val gson by lazy { Gson() }

    private val emptyDatabase get() =  Database(version = DATABASE_VERSION, emptyList())

    fun getLogs(): List<LogEntity> {
        val databaseString = fileManager.readFromFile(DATABASE)
        val database = gson.fromJson(databaseString, Database::class.java) ?: emptyDatabase

        return database.data
    }

    internal fun saveLog(event: LogEntity) {
        val databaseString = fileManager.readFromFile(DATABASE)
        val database = gson.fromJson(databaseString, Database::class.java) ?: emptyDatabase

        val newDatabase = database.copy(data = (database.data + event).distinct())
        val newDatabaseString = gson.toJson(newDatabase)

        fileManager.writeToFile(newDatabaseString, DATABASE)
    }

    private data class Database(val version: Int, val data: List<LogEntity>)

    companion object {
        private const val DATABASE = "test-workmanager.db"
        private const val DATABASE_VERSION = 1
    }
}