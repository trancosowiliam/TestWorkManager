package com.dalcim.testworkmanager.presentation.main

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.dalcim.testworkmanager.R
import com.dalcim.testworkmanager.service.ServiceRunner
import com.dalcim.testworkmanager.database.LogDatabase
import com.dalcim.testworkmanager.databinding.ActivityMainBinding
import com.dalcim.testworkmanager.presentation.config.ConfigActivity
import com.dalcim.testworkmanager.presentation.loglist.LogListActivity

class MainActivity : AppCompatActivity() {

    private val database by lazy { LogDatabase() }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartServer.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, ServiceRunner::class.java))
            } else {
                startService(Intent(this, ServiceRunner::class.java))
            }
        }

        binding.btnScheduleServer.setOnClickListener {
            scheduleTest()
        }

        binding.btnGoToLogList.setOnClickListener {
            LogListActivity.startActivity(this)
        }

        binding.btnGoToConfig.setOnClickListener {
            ConfigActivity.startActivity(this)
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