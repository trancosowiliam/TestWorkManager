package com.dalcim.testworkmanager.repository

import android.content.Context
import com.dalcim.testworkmanager.database.TestWorkManagerDatabase
import com.dalcim.testworkmanager.database.domain.ConfigEntity
import com.dalcim.testworkmanager.domain.WorkerConfig
import com.dalcim.testworkmanager.domain.WorkerConfig.Companion.EMPTY

class ConfigRepository(private val context: Context) {

    private val testWorkManagerDatabase by lazy { TestWorkManagerDatabase.buildDatabase(context) }
    private val configDao by lazy { testWorkManagerDatabase.configDao() }

    fun saveConfig(workerConfig: WorkerConfig) : Boolean {
        return configDao.insert(
            ConfigEntity.parse(workerConfig)
        ) > 0
    }

    fun getConfig() : WorkerConfig {
        return configDao.all().firstOrNull()?.let {
            ConfigEntity.parse(it)
        } ?: EMPTY
    }
}