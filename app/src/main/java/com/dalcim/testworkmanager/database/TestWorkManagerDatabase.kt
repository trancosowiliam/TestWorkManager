package com.dalcim.testworkmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dalcim.testworkmanager.database.domain.ConfigEntity
import com.dalcim.testworkmanager.database.domain.BreadcrumbEntity

@Database(entities = [ConfigEntity::class, BreadcrumbEntity::class], version = 2)
abstract class TestWorkManagerDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun breadcrumbDao(): BreadcrumbDao

    companion object {
        fun buildDatabase(context: Context): TestWorkManagerDatabase {
            return Room.databaseBuilder(
                context,
                TestWorkManagerDatabase::class.java,
                "database.db"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}