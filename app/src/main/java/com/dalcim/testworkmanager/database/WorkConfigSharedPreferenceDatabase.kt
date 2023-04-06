package com.dalcim.testworkmanager.database

import android.content.Context

class WorkConfigSharedPreferenceDatabase(context: Context) : WorkerConfigDatabase {
    private val sharedPreferences = context.getSharedPreferences(DATABASE, Context.MODE_PRIVATE)

    override fun lastJobRunTime(): Long {
        return sharedPreferences.getLong(TIME, 0)
    }

    override fun saveLastJobRunTime(time: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(TIME, time)
        editor.apply()
    }

    companion object {
        private const val DATABASE = "worker-config-database"
        private const val TIME = "lastJobRunTime"
    }
}