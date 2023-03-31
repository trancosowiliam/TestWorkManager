package com.dalcim.testworkmanager.domain

data class WorkerConfig(
    val frequency: Int,
    val frequencyUnit: FrequencyUnit,
    val successRatio: Int,
    val failureRatio: Int,
    val retryRatio: Int
) {
    enum class FrequencyUnit {
        HOUR,
        MINUTE
    }

    companion object {
        val EMPTY = WorkerConfig(
            frequency = 0,
            frequencyUnit = FrequencyUnit.MINUTE,
            successRatio = 0,
            failureRatio = 0,
            retryRatio = 0
        )
    }
}
