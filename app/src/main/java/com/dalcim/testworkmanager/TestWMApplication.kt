package com.dalcim.testworkmanager

import android.app.Application
import androidx.work.Configuration

class TestWMApplication : Application(), Configuration.Provider {
    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
}