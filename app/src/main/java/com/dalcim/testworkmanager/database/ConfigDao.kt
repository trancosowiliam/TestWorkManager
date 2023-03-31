package com.dalcim.testworkmanager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dalcim.testworkmanager.database.domain.ConfigEntity

@Dao
interface ConfigDao {
    @Query("SELECT * FROM ${ConfigEntity.TABLE_NAME}")
    fun all(): List<ConfigEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(configEntity: ConfigEntity): Long
}