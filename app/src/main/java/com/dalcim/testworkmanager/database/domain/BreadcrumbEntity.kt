package com.dalcim.testworkmanager.database.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dalcim.testworkmanager.database.domain.BreadcrumbEntity.Companion.TABLE_NAME
import com.dalcim.testworkmanager.domain.Breadcrumb
import java.util.*

@Entity(tableName = TABLE_NAME)
data class BreadcrumbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val where: String,
    val event: String,
    val time: Long
) {
    companion object {
        const val TABLE_NAME = "Breadcrumb"

        fun parse(breadcrumb: Breadcrumb): BreadcrumbEntity {
            return BreadcrumbEntity(
                id = 0,
                where = breadcrumb.where,
                event = breadcrumb.event,
                time = breadcrumb.date.time
            )
        }

        fun parse(breadcrumbEntity: BreadcrumbEntity): Breadcrumb {
            return Breadcrumb(
                where = breadcrumbEntity.where,
                event = breadcrumbEntity.event,
                date = Date(breadcrumbEntity.time)
            )
        }
    }
}