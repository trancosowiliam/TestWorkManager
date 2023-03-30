package com.dalcim.testworkmanager.ext

import android.app.Notification
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.dalcim.testworkmanager.R

fun Context.createForegroundNotification(channelId: String, @StringRes title: Int, @StringRes desc: Int, @DrawableRes icon: Int = R.drawable.ic_notification): Notification =
    NotificationCompat.Builder(this, channelId)
        .setSound(null)
        .setSmallIcon(icon)
        .setContentTitle(getString(title))
        .setContentText(getString(desc))
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOngoing(true)
        .build()
