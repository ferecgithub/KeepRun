package com.ferechamitbeyli.keeprun.framework.model.local.db

import androidx.room.*
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.keeprun.framework.model.local.entities.RunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunEntity(runEntity: RunEntity)

    @Delete
    suspend fun removeRunEntity(runEntity: RunEntity)

    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    fun getAllRunsSortedByDate(): Flow<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY time_in_millis DESC")
    fun getAllRunsSortedByTimeInMillis(): Flow<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY calories_burned DESC")
    fun getAllRunsSortedByCaloriesBurned(): Flow<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY avg_speed_in_kmh DESC")
    fun getAllRunsSortedByAverageSpeed(): Flow<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY distance_in_meters DESC")
    fun getAllRunsSortedByDistance(): Flow<List<RunEntity>>

    @Query("SELECT * FROM runs ORDER BY steps DESC")
    fun getAllRunsSortedByStepCount(): Flow<List<RunEntity>>

    @Query("SELECT SUM(time_in_millis) FROM runs")
    fun getTotalTimeInMillis(): Flow<Long>

    @Query("SELECT SUM(calories_burned) FROM runs")
    fun getTotalCaloriesBurned(): Flow<Int>

    @Query("SELECT SUM(distance_in_meters) FROM runs")
    fun getTotalDistanceInMeters(): Flow<Int>

    @Query("SELECT SUM(steps) FROM runs")
    fun getTotalStepCount(): Flow<Int>

    @Query("SELECT AVG(avg_speed_in_kmh) FROM runs")
    fun getTotalAverageSpeedInKMH(): Flow<Float>

}