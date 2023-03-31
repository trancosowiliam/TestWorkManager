package com.dalcim.testworkmanager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dalcim.testworkmanager.database.domain.BreadcrumbEntity

@Dao
interface BreadcrumbDao {
    @Query("SELECT * FROM ${BreadcrumbEntity.TABLE_NAME}")
    fun all(): List<BreadcrumbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(configEntity: BreadcrumbEntity): Long
}