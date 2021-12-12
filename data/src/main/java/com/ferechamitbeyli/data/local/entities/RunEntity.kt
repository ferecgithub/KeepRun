package com.ferechamitbeyli.data.local.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class RunEntity(
    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,

    val image: Bitmap? = null,

    val timestamp: Long = 0L,

    @ColumnInfo(name = "avg_speed_in_kmh")
    val avgSpeedInKMH: Double = 0.0,

    @ColumnInfo(name = "distance_in_meters")
    val distanceInMeters: Int = 0,

    @ColumnInfo(name = "time_in_millis")
    val timeInMillis: Long = 0L,

    @ColumnInfo(name = "calories_burned")
    val caloriesBurned: Int = 0,

    @ColumnInfo(name = "steps")
    val steps: Int = 0,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)