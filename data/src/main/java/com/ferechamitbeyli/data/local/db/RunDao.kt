package com.ferechamitbeyli.data.local.db

import androidx.room.*
import com.ferechamitbeyli.data.local.entities.RunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(runEntity: RunEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiple(vararg runEntity: RunEntity): List<Long>

    @Delete
    suspend fun remove(runEntity: RunEntity)

    @Query("DELETE FROM runs")
    suspend fun removeAll()

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

    @Query("SELECT SUM(time_in_millis) FROM runs WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getTotalTimeInMillisBetween(startDate: Long, endDate: Long): Flow<Long?>

    @Query("SELECT SUM(calories_burned) FROM runs WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getTotalCaloriesBurnedBetween(startDate: Long, endDate: Long): Flow<Int?>

    @Query("SELECT SUM(distance_in_meters) FROM runs WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getTotalDistanceInMetersBetween(startDate: Long, endDate: Long): Flow<Int?>

    @Query("SELECT AVG(avg_speed_in_kmh) FROM runs WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getTotalAverageSpeedInKMHBetween(startDate: Long, endDate: Long): Flow<Double?>

    @Query("SELECT SUM(steps) FROM runs WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getTotalStepCountBetween(startDate: Long, endDate: Long): Flow<Int?>

}