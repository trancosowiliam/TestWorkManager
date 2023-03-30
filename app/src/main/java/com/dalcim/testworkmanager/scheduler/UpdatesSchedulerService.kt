package com.dalcim.testworkmanager.scheduler

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.boot.WorkerInitializer
import com.dalcim.testworkmanager.database.LogDatabase
import com.dalcim.testworkmanager.domain.LogEntity
import com.dalcim.testworkmanager.ext.createForegroundNotification
import com.dalcim.testworkmanager.notifier.createChannelIfNeeded

class UpdatesSchedulerService : Service() {

    private val workerInitializer: WorkerInitializer by lazy { WorkerInitializer() }
    private val database by lazy { LogDatabase() }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        database.saveLog(LogEntity("UpdatesSchedulerService.onStartCommand"))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, createNotification())
        }

        if (intent?.action == ACTION_SCHEDULE_UPDATES) {
            workerInitializer.scheduleUpdates(this)
        }

        stopSelf(startId)
        return START_STICKY
    }

    private fun createNotification(): Notification {
        applicationContext.createChannelIfNeeded(CHANNEL_ID, CHANNEL_NAME)
        return applicationContext.createForegroundNotification(
            CHANNEL_ID,
            R.string.notification_foreground_scheduling_update,
            R.string.notification_foreground_service_running
        )
    }

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val ACTION_SCHEDULE_UPDATES = "action-schedule-updates"
        private const val CHANNEL_ID = "channel-scheduler"
        private const val CHANNEL_NAME = "scheduler"
        private const val NOTIFICATION_ID = 0x142
    }
}
