package com.ferechamitbeyli.domain.repository.datasources.home

import com.ferechamitbeyli.domain.entity.Run
import kotlinx.coroutines.flow.Flow

interface RunLocalDataSource {
    suspend fun insertToDB(run: Run)
    suspend fun removeFromDB(run: Run)

    /** Get all functions **/
    suspend fun getAllRunsSortedByDate(): Flow<List<Run>>
    suspend fun getAllRunsSortedByTimeInMillis(): Flow<List<Run>>
    suspend fun getAllRunsSortedByCaloriesBurned(): Flow<List<Run>>
    suspend fun getAllRunsSortedByAverageSpeed(): Flow<List<Run>>
    suspend fun getAllRunsSortedByDistance(): Flow<List<Run>>

    /** Get total functions **/
    suspend fun getTotalTimeInMillis(): Flow<Long>
    suspend fun getTotalCaloriesBurned(): Flow<Int>
    suspend fun getTotalDistanceInMeters(): Flow<Int>
    suspend fun getTotalAverageSpeedInKMH(): Flow<Double>
    suspend fun getTotalStepCount(): Flow<Int>
}