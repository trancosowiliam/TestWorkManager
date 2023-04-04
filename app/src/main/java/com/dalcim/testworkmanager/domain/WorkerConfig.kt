package com.dalcim.testworkmanager.domain

data class WorkerConfig(
    val interval: Int,
    val intervalUnit: IntervalUnit,
    val retryInterval: Int,
    val retryIntervalUnit: IntervalUnit,
    val retryPolicy: RetryPolicy,
    val successRatio: Int,
    val failureRatio: Int,
    val retryRatio: Int
) {
    enum class IntervalUnit {
        MINUTE,
        HOUR
    }

    enum class RetryPolicy {
        EXPONENTIAL,
        LINEAR
    }

    companion object {
        val EMPTY = WorkerConfig(
            interval = 12,
            intervalUnit = IntervalUnit.HOUR,
            retryInterval = 6,
            retryIntervalUnit = IntervalUnit.HOUR,
            retryPolicy = RetryPolicy.LINEAR,
            successRatio = 100,
            failureRatio = 0,
            retryRatio = 0
        )
    }
}
