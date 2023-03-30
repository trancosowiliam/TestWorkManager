package com.dalcim.testworkmanager.presentation

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.boot.ServiceRunner
import com.dalcim.testworkmanager.database.LogDatabase
import com.dalcim.testworkmanager.domain.LogEntity
import com.dalcim.testworkmanager.ext.format
import com.dalcim.testworkmanager.presentation.loglist.LogListActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val database by lazy { LogDatabase() }
    private val btnStartServer by lazy { findViewById<AppCompatButton>(R.id.btnStartServer) }
    private val btnScheduleServer by lazy { findViewById<AppCompatButton>(R.id.btnScheduleServer) }
    private val btnShowLog by lazy { findViewById<AppCompatButton>(R.id.btnShowLog) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartServer.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, ServiceRunner::class.java))
            } else {
                startService(Intent(this, ServiceRunner::class.java))
            }
        }

        btnScheduleServer.setOnClickListener {
            scheduleTest()
        }

        btnShowLog.setOnClickListener {
            LogListActivity.startActivity(this)
        }


    }

    private fun scheduleTest() {
        val updatesJobIntent = Intent().apply {
            component =
                ComponentName(PDM_PKG_NAME, "$PDM_PKG_NAME.scheduler.UpdatesSchedulerService")
            action = PDM_UPDATES_SCHEDULER_ACTION
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(updatesJobIntent)
        } else {
            startService(updatesJobIntent)
        }
    }

    companion object {
        private const val PDM_PKG_NAME = "com.dalcim.testworkmanager"
        private const val PDM_UPDATES_SCHEDULER_ACTION = "action-schedule-test"

    }
}