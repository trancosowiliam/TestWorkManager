package com.dalcim.testworkmanager.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.domain.Breadcrumb
import com.dalcim.testworkmanager.ext.createForegroundNotification
import com.dalcim.testworkmanager.notifier.createChannelIfNeeded
import com.dalcim.testworkmanager.repository.BreadcrumbRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.concurrent.TimeUnit

class ServiceRunner : Service() {

    private val repository by lazy { BreadcrumbRepository(this) }

    @DelicateCoroutinesApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        repository.addBreadcrumb(Breadcrumb("ServiceRunner", "onStartCommand entry"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, createNotification())
        }

        runUpdateService(applicationContext, "fromStartCommand", true)

        stopSelf(startId)
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        applicationContext.createChannelIfNeeded(BOOT_CHANNEL_ID, BOOT_CHANNEL_NAME)
        return applicationContext.createForegroundNotification(
            BOOT_CHANNEL_ID,
            R.string.notification_foreground_checking_updates,
            R.string.notification_foreground_scheduling_update,
            android.R.drawable.stat_sys_download
        )
    }

    companion object {
        private const val BOOT_CHANNEL_ID = "channel-boot"
        private const val BOOT_CHANNEL_NAME = "boot"
        private const val SAFER_DELAY_STARTUP_WORKER = 2L // in seconds
        private const val NOTIFICATION_ID = 0x139


        fun runUpdateService(
            applicationContext: Context,
            executionFrom: String,
            isWithDelay: Boolean = false
        ) {
            val data = Data.Builder().apply {
                putString("from", "startCommand")
            }
            val workerRequest = OneTimeWorkRequestBuilder<TestWorker>().apply {
                addTag(executionFrom)
                setInputData(data.build())
                if (isWithDelay) setInitialDelay(SAFER_DELAY_STARTUP_WORKER, TimeUnit.SECONDS)
            }.build()

            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                ServiceRunner::class.java.name,
                ExistingWorkPolicy.KEEP,
                workerRequest
            )
        }
    }
}
