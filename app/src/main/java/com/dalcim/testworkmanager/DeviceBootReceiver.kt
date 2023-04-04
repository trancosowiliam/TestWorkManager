package com.dalcim.testworkmanager

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dalcim.testworkmanager.domain.Breadcrumb
import com.dalcim.testworkmanager.repository.BreadcrumbRepository
import com.dalcim.testworkmanager.service.ServiceRunner

class DeviceBootReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val breadcrumbRepository = BreadcrumbRepository(context)
        breadcrumbRepository.addBreadcrumb(Breadcrumb("DeviceBootReceiver", "onReceive entry"))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, ServiceRunner::class.java))
        } else {
            context.startService(Intent(context, ServiceRunner::class.java))
        }
    }
}
