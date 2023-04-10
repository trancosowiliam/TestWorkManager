package com.dalcim.testworkmanager.service

import android.content.Context
import androidx.work.*
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.database.WorkConfigSharedPreferenceDatabase
import com.dalcim.testworkmanager.database.WorkerConfigDatabase
import com.dalcim.testworkmanager.domain.Breadcrumb
import com.dalcim.testworkmanager.domain.WorkerConfig
import com.dalcim.testworkmanager.ext.createForegroundNotification
import com.dalcim.testworkmanager.ext.format
import com.dalcim.testworkmanager.notifier.createChannelIfNeeded
import com.dalcim.testworkmanager.repository.BreadcrumbRepository
import com.dalcim.testworkmanager.repository.ConfigRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.random.Random

// PdmWorker
class TestWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val breadcrumbRepository by lazy { BreadcrumbRepository(context) }
    private val configRepository by lazy { ConfigRepository(context) }
    private val workerConfigDatabase: WorkerConfigDatabase by lazy {
        WorkConfigSharedPreferenceDatabase(
            context
        )
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        breadcrumbRepository.addBreadcrumb(Breadcrumb("TestWorker", "doWork entry"))
        val config = configRepository.getConfig()

        val executionFrom = inputData.getString(EXECUTOR_FROM_KEY)
        val isFromApplication = executionFrom == FROM_APP
        val isFromScheduler = executionFrom == FROM_SCHEDULER
        val workersScheduler = getWorkersFrom(TestWorker::class.java.name)
        val fromApplicationRule =
            workersScheduler.any { it.state == WorkInfo.State.RUNNING } && isFromApplication

        breadcrumbRepository.addBreadcrumb(Breadcrumb("doWork", "executionFrom: $executionFrom"))

        if (isFromScheduler && shouldRunning().not()) {
            breadcrumbRepository.addBreadcrumb(Breadcrumb("TestWorker", "skip"))
            return@withContext randomizeResult(config)
        } else {
            breadcrumbRepository.addBreadcrumb(Breadcrumb("TestWorker", "run"))
            workerConfigDatabase.saveLastJobRunTime(Date().time)
        }

        val applicationWorkers = getWorkersFrom(ServiceRunner::class.java.name)
        val fromSchedulerRule =
            applicationWorkers.any { it.state == WorkInfo.State.RUNNING } && isFromScheduler

        if (fromApplicationRule || fromSchedulerRule) {
            return@withContext Result.success().also {
                breadcrumbRepository.addBreadcrumb(Breadcrumb("TestWorker", "doWork running"))
                breadcrumbRepository.addBreadcrumb(
                    Breadcrumb(
                        "TestWorker",
                        "Result.success returned"
                    )
                )
            }
        }

        setForeground(createForegroundInfo())

        return@withContext randomizeResult(config)
    }

    private suspend fun getWorkersFrom(uniqueName: String): List<WorkInfo> {
        return WorkManager.getInstance(applicationContext).getWorkInfosForUniqueWork(uniqueName)
            .await()
    }

    private fun randomizeResult(config: WorkerConfig): Result {
        val total = config.successRatio + config.failureRatio + config.retryRatio

        val random = Random.nextInt(total)

        return when {
            random < config.successRatio -> {
                breadcrumbRepository.addBreadcrumb(
                    Breadcrumb(
                        "TestWorker",
                        "Result.successRatio returned"
                    )
                )
                Result.success()
            }
            random < config.successRatio + config.failureRatio -> {
                breadcrumbRepository.addBreadcrumb(
                    Breadcrumb(
                        "TestWorker",
                        "Result.failure returned"
                    )
                )
                Result.failure()
            }
            else -> {
                breadcrumbRepository.addBreadcrumb(
                    Breadcrumb(
                        "TestWorker",
                        "Result.retry returned"
                    )
                )
                Result.retry()
            }
        }
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

    private fun shouldRunning(): Boolean {
        val minimalIntervalBetweenWorkInMinutes = 120
        val timestampToWork = minimalIntervalBetweenWorkInMinutes * 60 * 1000
        val lastJobRunTime = workerConfigDatabase.lastJobRunTime()

        val fromDate = lastJobRunTime + timestampToWork

        breadcrumbRepository.addBreadcrumb(Breadcrumb("shouldRunning", "time: ${Date().format()}"))
        breadcrumbRepository.addBreadcrumb(Breadcrumb("shouldRunning", "from: ${Date(fromDate).format()}"))

        return Date().time > fromDate
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
