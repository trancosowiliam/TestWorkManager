package com.dalcim.testworkmanager.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.work.*
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.domain.Breadcrumb
import com.dalcim.testworkmanager.domain.WorkerConfig
import com.dalcim.testworkmanager.ext.createForegroundNotification
import com.dalcim.testworkmanager.notifier.createChannelIfNeeded
import com.dalcim.testworkmanager.repository.BreadcrumbRepository
import com.dalcim.testworkmanager.repository.ConfigRepository
import com.dalcim.testworkmanager.service.TestWorker.Companion.EXECUTOR_FROM_KEY
import com.dalcim.testworkmanager.service.TestWorker.Companion.FROM_SCHEDULER
import java.util.concurrent.TimeUnit

// UpdatesSchedulerService
class ServiceScheduler: Service() {

    private val breadcrumbRepository by lazy { BreadcrumbRepository(this) }
    private val configRepository by lazy { ConfigRepository(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        breadcrumbRepository.addBreadcrumb(Breadcrumb("ServiceScheduler", "onStartCommand entry"))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, createNotification())
        }

        if (intent?.action == ACTION_SCHEDULE_UPDATES) {
            scheduleUpdates(this)
        }

        stopSelf(startId)
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private fun scheduleUpdates(context: Context) {
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

        val config = configRepository.getConfig()

        val interval = config.interval.toLong()

        val intervalUnit = when (config.intervalUnit) {
            WorkerConfig.IntervalUnit.MINUTE -> TimeUnit.MINUTES
            else -> TimeUnit.HOURS
        }

        val retryInterval = config.retryInterval.toLong()

        val retryIntervalUnit = when (config.retryIntervalUnit) {
            WorkerConfig.IntervalUnit.MINUTE -> TimeUnit.MINUTES
            else -> TimeUnit.HOURS
        }

        val retryPolicy = when (config.retryPolicy) {
            WorkerConfig.RetryPolicy.EXPONENTIAL -> BackoffPolicy.EXPONENTIAL
            WorkerConfig.RetryPolicy.LINEAR -> BackoffPolicy.LINEAR
        }

        breadcrumbRepository.addBreadcrumb(Breadcrumb("ServiceScheduler", "createPeriodicWorkRequest interval:$interval, timeUnit:$intervalUnit"))

        val data = Data.Builder().apply {
            putString(EXECUTOR_FROM_KEY, FROM_SCHEDULER)
        }

        return PeriodicWorkRequestBuilder<TestWorker>(interval, intervalUnit).apply {
            addTag("fromScheduler")
            setConstraints(constraints)
            setBackoffCriteria(retryPolicy, retryInterval, retryIntervalUnit)
            setInputData(data.build())
        }.build().also {
            breadcrumbRepository.addBreadcrumb(Breadcrumb("ServiceScheduler.PeriodicWorkRequestBuilder", "id: ${it.id}"))
        }
    }

    private fun createNotification(): Notification {
        applicationContext.createChannelIfNeeded(CHANNEL_ID, CHANNEL_NAME)
        return applicationContext.createForegroundNotification(
            CHANNEL_ID,
            R.string.notification_foreground_service_running,
            R.string.notification_foreground_service_running
        )
    }

    companion object {
        const val ACTION_SCHEDULE_UPDATES = "action-schedule-test"
        private const val CHANNEL_ID = "channel-scheduler"
        private const val CHANNEL_NAME = "scheduler"
        private const val NOTIFICATION_ID = 0x142
    }

}