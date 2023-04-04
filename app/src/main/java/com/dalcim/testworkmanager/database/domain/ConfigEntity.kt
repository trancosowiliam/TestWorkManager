package com.dalcim.testworkmanager.database.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dalcim.testworkmanager.database.domain.ConfigEntity.Companion.TABLE_NAME
import com.dalcim.testworkmanager.domain.WorkerConfig

@Entity(tableName = TABLE_NAME)
data class ConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val interval: Int,
    val intervalUnit: Int,
    val retryInterval: Int,
    val retryIntervalUnit: Int,
    val retryPolicy: Int,
    val successRatio: Int,
    val failureRatio: Int,
    val retryRatio: Int
) {
    companion object {
        const val TABLE_NAME = "Config"

        private const val MINUTE_ID = 0
        private const val HOUR_ID = 1

        private const val EXPONENTIAL_ID = 0
        private const val LINEAR_ID = 1

        fun parse(workerConfig: WorkerConfig): ConfigEntity {
            return ConfigEntity(
                id = 1,
                interval = workerConfig.interval,
                intervalUnit = when (workerConfig.intervalUnit) {
                    WorkerConfig.IntervalUnit.MINUTE -> MINUTE_ID
                    WorkerConfig.IntervalUnit.HOUR -> HOUR_ID
                },
                retryInterval = workerConfig.retryInterval,
                retryIntervalUnit = when (workerConfig.retryIntervalUnit) {
                    WorkerConfig.IntervalUnit.MINUTE -> MINUTE_ID
                    WorkerConfig.IntervalUnit.HOUR -> HOUR_ID
                },
                retryPolicy = when (workerConfig.retryPolicy) {
                    WorkerConfig.RetryPolicy.EXPONENTIAL -> EXPONENTIAL_ID
                    WorkerConfig.RetryPolicy.LINEAR -> LINEAR_ID
                },
                successRatio = workerConfig.successRatio,
                failureRatio = workerConfig.failureRatio,
                retryRatio = workerConfig.retryRatio
            )
        }

        fun parse(configEntity: ConfigEntity): WorkerConfig {
            return WorkerConfig(
                interval = configEntity.interval,
                intervalUnit = when (configEntity.intervalUnit) {
                    MINUTE_ID -> WorkerConfig.IntervalUnit.MINUTE
                    else -> WorkerConfig.IntervalUnit.HOUR
                },
                retryInterval = configEntity.retryInterval,
                retryIntervalUnit = when (configEntity.retryIntervalUnit) {
                    MINUTE_ID -> WorkerConfig.IntervalUnit.MINUTE
                    else -> WorkerConfig.IntervalUnit.HOUR
                },
                retryPolicy = when (configEntity.retryPolicy) {
                    EXPONENTIAL_ID -> WorkerConfig.RetryPolicy.EXPONENTIAL
                    else -> WorkerConfig.RetryPolicy.LINEAR
                },
                successRatio = configEntity.successRatio,
                failureRatio = configEntity.failureRatio,
                retryRatio = configEntity.retryRatio
            )
        }
    }
}