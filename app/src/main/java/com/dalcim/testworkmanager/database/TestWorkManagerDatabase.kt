package com.dalcim.testworkmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dalcim.testworkmanager.database.domain.ConfigEntity

@Database(entities = [ConfigEntity::class], version = 1)
abstract class TestWorkManagerDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao

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