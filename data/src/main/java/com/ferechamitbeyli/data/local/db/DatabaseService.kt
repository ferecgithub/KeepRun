package com.ferechamitbeyli.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ferechamitbeyli.data.local.entities.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1,
    exportSchema = true
)
abstract class DatabaseService: RoomDatabase() {

    abstract fun getRunDao(): RunDao
}