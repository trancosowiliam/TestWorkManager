package com.dalcim.testworkmanager.database.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dalcim.testworkmanager.database.domain.ConfigEntity.Companion.TABLE_NAME
import com.dalcim.testworkmanager.domain.WorkerConfig

@Entity(tableName = TABLE_NAME)
data class ConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val frequency: Int,
    val frequencyUnit: Int,
    val successRatio: Int,
    val failureRatio: Int,
    val retryRatio: Int
) {
    companion object {
        const val TABLE_NAME = "Config"

        const val MINUTE_ID = 0
        const val HOUR_ID = 1

        fun parse(workerConfig: WorkerConfig): ConfigEntity {
            return ConfigEntity(
                id = 1,
                frequency = workerConfig.frequency,
                frequencyUnit = when (workerConfig.frequencyUnit) {
                    WorkerConfig.FrequencyUnit.MINUTE -> MINUTE_ID
                    WorkerConfig.FrequencyUnit.HOUR -> HOUR_ID
                },
                successRatio = workerConfig.successRatio,
                failureRatio = workerConfig.failureRatio,
                retryRatio = workerConfig.retryRatio
            )
        }

        fun parse(configEntity: ConfigEntity): WorkerConfig {
            return WorkerConfig(
                frequency = configEntity.frequency,
                frequencyUnit = when (configEntity.frequencyUnit) {
                    MINUTE_ID -> WorkerConfig.FrequencyUnit.MINUTE
                    else -> WorkerConfig.FrequencyUnit.HOUR
                },
                successRatio = configEntity.successRatio,
                failureRatio = configEntity.failureRatio,
                retryRatio = configEntity.retryRatio
            )
        }
    }
}