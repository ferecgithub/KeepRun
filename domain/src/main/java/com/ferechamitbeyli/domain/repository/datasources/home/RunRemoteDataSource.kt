package com.ferechamitbeyli.domain.repository.datasources.home

import com.ferechamitbeyli.domain.entity.Run
import kotlinx.coroutines.flow.Flow

interface RunRemoteDataSource {
    suspend fun insertToRemote(run: Run)
    suspend fun removeFromRemote(run: Run)



    /** Get all functions **/
    suspend fun getAllRunsFromRemote(): Flow<List<Run>>

    /** Get total functions **/
    suspend fun getTotalTimeInMillisFromRemote(): Flow<Long>
    suspend fun getTotalCaloriesBurnedFromRemote(): Flow<Int>
    suspend fun getTotalDistanceInMetersFromRemote(): Flow<Int>
    suspend fun getTotalAverageSpeedInKMHFromRemote(): Flow<Float>
    suspend fun getTotalStepCountFromRemote(): Flow<Int>


}