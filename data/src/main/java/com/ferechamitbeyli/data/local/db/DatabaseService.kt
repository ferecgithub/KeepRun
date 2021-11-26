package com.ferechamitbeyli.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ferechamitbeyli.data.local.entities.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(TypeConverter::class)
abstract class DatabaseService: RoomDatabase() {

    abstract fun getRunDao(): RunDao
}