package com.ferechamitbeyli.keeprun.framework.model.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ferechamitbeyli.domain.entity.Run

@Entity(tableName = "runs")
data class RunEntity(
    var imageUrl: String? = null,
    var timestamp: Long = 0L,

    @ColumnInfo(name = "avg_speed_in_kmh")
    var avgSpeedInKMH: Float = 0f,

    @ColumnInfo(name = "distance_in_meters")
    var distanceInMeters: Int = 0,

    @ColumnInfo(name = "time_in_millis")
    var timeInMillis: Long = 0L,

    @ColumnInfo(name = "calories_burned")
    var caloriesBurned: Int = 0,

    @ColumnInfo(name = "steps")
    var steps: Int = 0,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
) {
    companion object {
        fun fromRun(run: Run)
            = RunEntity(
            run.imageUrl,
            run.timestamp,
            run.avgSpeedInKMH,
            run.distanceInMeters,
            run.timeInMillis,
            run.caloriesBurned,
            run.steps
        )
    }

    fun toRun()
        = Run(imageUrl, timestamp, avgSpeedInKMH, distanceInMeters, timeInMillis, caloriesBurned, steps, id)
}