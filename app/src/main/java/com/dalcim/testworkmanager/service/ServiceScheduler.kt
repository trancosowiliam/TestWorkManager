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
import com.dalcim.testworkmanager.ext.createForegroundNotification
import com.dalcim.testworkmanager.notifier.createChannelIfNeeded
import com.dalcim.testworkmanager.repository.BreadcrumbRepository
import java.util.concurrent.TimeUnit

class ServiceScheduler: Service() {

    private val repository by lazy { BreadcrumbRepository(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        repository.addBreadcrumb(Breadcrumb("ServiceScheduler", "onStartCommand entry"))

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

        repository.addBreadcrumb(Breadcrumb("ServiceScheduler", "createPeriodicWorkRequest interval:$interval, timeUnit:$timeUnit"))

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

    private fun createNotification(): Notification {
        applicationContext.createChannelIfNeeded(CHANNEL_ID, CHANNEL_NAME)
        return applicationContext.createForegroundNotification(
            CHANNEL_ID,
            R.string.notification_foreground_service_running,
            R.string.notification_foreground_service_running
        )
    }

    companion object {
        private const val RELEASE_JOB_INTERVAL_IN_HOURS = 12L
        private const val DEBUG_JOB_INTERVAL_IN_MINUTES = 15L
        private const val RETRY_JOB_INTERVAL_IN_HOURS = 6L

        const val ACTION_SCHEDULE_UPDATES = "action-schedule-test"
        private const val CHANNEL_ID = "channel-scheduler"
        private const val CHANNEL_NAME = "scheduler"
        private const val NOTIFICATION_ID = 0x142
    }

}