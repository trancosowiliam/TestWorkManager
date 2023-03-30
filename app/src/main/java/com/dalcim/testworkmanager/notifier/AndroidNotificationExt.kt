package com.dalcim.testworkmanager.notifier

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

fun Context.createChannelIfNeeded(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setSound(null, null)
            NotificationManagerCompat.from(applicationContext).createNotificationChannel(this)
        }
    }
}

