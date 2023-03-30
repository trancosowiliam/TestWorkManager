package com.dalcim.testworkmanager.domain

import java.util.*

data class LogEntity(
    val event: String,
    val time: Long
) {
    constructor(event: String) : this(event, Date().time)
}
