package com.dalcim.testworkmanager.boot

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dalcim.testworkmanager.database.LogDatabase
import com.dalcim.testworkmanager.domain.LogEntity
import java.util.concurrent.TimeUnit

class WorkerInitializer {

    private val database by lazy { LogDatabase() }

    fun scheduleUpdates(context: Context) {
        database.saveLog(LogEntity("WorkerInitializer.scheduleUpdates"))

        val repeatingRequest = createPeriodicWorkRequest()
        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
            TestWorker::class.java.name,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    private fun createPeriodicWorkRequest(): PeriodicWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val interval = if (false) {
            DEBUG_JOB_INTERVAL_IN_MINUTES
        } else {
            RELEASE_JOB_INTERVAL_IN_HOURS
        }

        val timeUnit = if (false) {
            TimeUnit.MINUTES
        } else {
            TimeUnit.HOURS
        }

        val data = Data.Builder().apply {
            putString("from", "scheduler")
        }

        return PeriodicWorkRequestBuilder<TestWorker>(interval, timeUnit).apply {
            addTag("fromScheduler")
            setConstraints(constraints)
            setBackoffCriteria(BackoffPolicy.LINEAR, RETRY_JOB_INTERVAL_IN_HOURS, timeUnit)
            setInputData(data.build())
        }.build()
    }

    companion object {
        private const val RELEASE_JOB_INTERVAL_IN_HOURS = 12L
        private const val DEBUG_JOB_INTERVAL_IN_MINUTES = 15L
        private const val RETRY_JOB_INTERVAL_IN_HOURS = 6L
    }
}
