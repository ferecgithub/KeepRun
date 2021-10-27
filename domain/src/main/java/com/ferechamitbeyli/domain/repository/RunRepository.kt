package com.ferechamitbeyli.domain.repository

import com.ferechamitbeyli.domain.entity.Run
import kotlinx.coroutines.flow.Flow

interface RunRepository {
    suspend fun insert(run: Run)
    suspend fun remove(run: Run)

    /*

    /** Get all functions from remote end-point **/
    suspend fun getAllRunsFromRemote(): Flow<List<Run>>

    /** Get total functions from remote end-point **/
    suspend fun getTotalTimeInMillisFromRemote(): Flow<Long>
    suspend fun getTotalCaloriesBurnedFromRemote(): Flow<Int>
    suspend fun getTotalDistanceInMetersFromRemote(): Flow<Int>
    suspend fun getTotalAverageSpeedInKMHFromRemote(): Flow<Float>
    suspend fun getTotalStepCountFromRemote(): Flow<Int>

    */

    /** Get all functions **/
    suspend fun getAllRunsSortedByDate() : Flow<List<Run>>
    suspend fun getAllRunsSortedByTimeInMillis() : Flow<List<Run>>
    suspend fun getAllRunsSortedByCaloriesBurned() : Flow<List<Run>>
    suspend fun getAllRunsSortedByAverageSpeed() : Flow<List<Run>>
    suspend fun getAllRunsSortedByDistance() : Flow<List<Run>>

    /** Get total functions **/
    suspend fun getTotalTimeInMillis() : Flow<Long>
    suspend fun getTotalCaloriesBurned() : Flow<Int>
    suspend fun getTotalDistanceInMeters() : Flow<Int>
    suspend fun getTotalAverageSpeedInKMH() : Flow<Float>
    suspend fun getTotalStepCount() : Flow<Int>



}