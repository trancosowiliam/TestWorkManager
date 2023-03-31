package com.dalcim.testworkmanager.domain

import com.dalcim.testworkmanager.database.domain.BreadcrumbEntity
import java.util.*


data class Breadcrumb(
    val where: String,
    val event: String,
    val date: Date
) {
    constructor(where: String, event: String) : this(where, event, Date())
}