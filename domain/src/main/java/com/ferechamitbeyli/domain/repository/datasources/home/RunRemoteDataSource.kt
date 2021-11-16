package com.ferechamitbeyli.domain.repository.datasources.home

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.Run
import kotlinx.coroutines.flow.Flow

interface RunRemoteDataSource {

    suspend fun insertMapImageToRemoteDB(byteArray: ByteArray, timestamp: String): Flow<Resource<String>>
    suspend fun removeMapImageFromRemoteDB(timestamp: String): Flow<Resource<String>>

    suspend fun insertRunToRemoteDB(run: Run, imageUrl: String): Flow<Resource<String>>
    suspend fun removeRunFromRemoteDB(run: Run): Flow<Resource<String>>

    /** Get all functions **/
    suspend fun getAllRunsFromRemoteDB(): Flow<Resource<List<Run>>>

    /*
    /** Get total functions **/
    suspend fun getTotalTimeInMillisFromRemote(): Flow<Resource<Long>>
    suspend fun getTotalCaloriesBurnedFromRemote(): Flow<Resource<Int>>
    suspend fun getTotalDistanceInMetersFromRemote(): Flow<Resource<Int>>
    suspend fun getTotalAverageSpeedInKMHFromRemote(): Flow<Resource<Double>>
    suspend fun getTotalStepCountFromRemote(): Flow<Resource<Int>>

     */


}