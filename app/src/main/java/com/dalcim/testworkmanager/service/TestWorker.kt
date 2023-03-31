package com.dalcim.testworkmanager.service

import android.content.Context
import androidx.work.*
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.domain.Breadcrumb
import com.dalcim.testworkmanager.ext.createForegroundNotification
import com.dalcim.testworkmanager.notifier.createChannelIfNeeded
import com.dalcim.testworkmanager.repository.BreadcrumbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val repository by lazy { BreadcrumbRepository(context) }


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        repository.addBreadcrumb(Breadcrumb("TestWorker", "doWork entry"))

        val executionFrom = inputData.getString(EXECUTOR_FROM_KEY)
        val isFromApplication = executionFrom == FROM_APP
        val isFromScheduler = executionFrom == FROM_SCHEDULER
        val workersScheduler = getWorkersFrom(TestWorker::class.java.name)
        val fromApplicationRule = workersScheduler.any { it.state == WorkInfo.State.RUNNING } && isFromApplication

        val applicationWorkers = getWorkersFrom(ServiceRunner::class.java.name)
        val fromSchedulerRule = applicationWorkers.any { it.state == WorkInfo.State.RUNNING } && isFromScheduler

        if (fromApplicationRule || fromSchedulerRule) {
            return@withContext Result.success().also {
                repository.addBreadcrumb(Breadcrumb("TestWorker", "doWork running"))
                repository.addBreadcrumb(Breadcrumb("TestWorker", "Result.success returned"))
            }
        }

        setForeground(createForegroundInfo())

        return@withContext Result.retry().also {
            repository.addBreadcrumb(Breadcrumb("TestWorker", "Result.retry returned"))
        }
    }

    private suspend fun getWorkersFrom(uniqueName: String): List<WorkInfo> {
        return WorkManager.getInstance(applicationContext).getWorkInfosForUniqueWork(uniqueName).await()
    }

    private fun createForegroundInfo(): ForegroundInfo {
        applicationContext.createChannelIfNeeded(CHANNEL_ID, CHANNEL_NAME)
        val notification = applicationContext.createForegroundNotification(
            CHANNEL_ID,
            R.string.notification_foreground_checking_updates,
            R.string.notification_foreground_service_running
        )

        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    companion object {
        const val EXECUTOR_FROM_KEY = "FROM"
        const val FROM_SCHEDULER = "SCHEDULER"
        const val FROM_APP = "APP"

        private const val NOTIFICATION_ID = 0x143
        private const val CHANNEL_ID = "channel"
        private const val CHANNEL_NAME = "test-workmanager"
    }
}
