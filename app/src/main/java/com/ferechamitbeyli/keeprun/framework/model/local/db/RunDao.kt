package com.ferechamitbeyli.keeprun.framework.model.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ferechamitbeyli.keeprun.framework.model.local.entities.RunEntity

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunEntity(runEntity: RunEntity)

    @Delete
    suspend fun removeRunEntity(runEntity: RunEntity)

    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    fun getAllRunsSortedByDate(): LiveData<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY time_in_millis DESC")
    fun getAllRunsSortedByTimeInMillis(): LiveData<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY calories_burned DESC")
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY avg_speed_in_kmh DESC")
    fun getAllRunsSortedByAverageSpeed(): LiveData<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY distance_in_meters DESC")
    fun getAllRunsSortedByDistance(): LiveData<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY steps DESC")
    fun getAllRunsSortedByStepCount(): LiveData<List<RunEntity>>


    @Query("SELECT SUM(time_in_millis) FROM runs")
    fun getTotalTimeInMillis(): LiveData<Long>

    @Query("SELECT SUM(calories_burned) FROM runs")
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("SELECT SUM(distance_in_meters) FROM runs")
    fun getTotalDistanceInMeters(): LiveData<Int>

    @Query("SELECT SUM(steps) FROM runs")
    fun getTotalStepCount(): LiveData<Int>

    @Query("SELECT AVG(avg_speed_in_kmh) FROM runs")
    fun getTotalAverageSpeedInKMH(): LiveData<Float>

}