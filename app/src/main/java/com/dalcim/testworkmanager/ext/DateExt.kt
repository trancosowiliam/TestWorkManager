package com.dalcim.testworkmanager.ext

import java.text.SimpleDateFormat
import java.util.*

const val BEAUTIFUL_DATE = "dd-MM-yyyy HH:mm:ss"

fun Date.format(format: String = BEAUTIFUL_DATE): String {
    return SimpleDateFormat(format, Locale.US).format(this)
}