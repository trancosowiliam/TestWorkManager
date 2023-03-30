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
}
