package com.dalcim.testworkmanager.database

interface WorkerConfigDatabase {
    fun lastJobRunTime(): Long
    fun saveLastJobRunTime(time: Long)
}