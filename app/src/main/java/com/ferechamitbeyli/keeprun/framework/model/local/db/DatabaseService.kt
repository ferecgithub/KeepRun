package com.ferechamitbeyli.keeprun.framework.model.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ferechamitbeyli.keeprun.framework.model.local.entities.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1
    //exportSchema = false
)
abstract class DatabaseService: RoomDatabase() {

    abstract fun getRunDao(): RunDao
}