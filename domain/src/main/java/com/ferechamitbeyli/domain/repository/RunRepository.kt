package com.ferechamitbeyli.domain.repository

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.Run
import kotlinx.coroutines.flow.Flow

interface RunRepository {

    /**
     * Functions fetches and saves data from/to Room Database
     */
    suspend fun insertRunToDB(run: Run)
    suspend fun insertMultipleRunsToDB(vararg run: Run)
    suspend fun removeRunFromDB(run: Run)
    suspend fun removeAllRunsFromDB()



    /*
    suspend fun getAllRunsSortedByDate(): Flow<List<Run>>
    suspend fun getAllRunsSortedByTimeInMillis(): Flow<List<Run>>
    suspend fun getAllRunsSortedByCaloriesBurned(): Flow<List<Run>>
    suspend fun getAllRunsSortedByAverageSpeed(): Flow<List<Run>>
    suspend fun getAllRunsSortedByDistance(): Flow<List<Run>>

    suspend fun getTotalTimeInMillis(): Flow<Long>
    suspend fun getTotalCaloriesBurned(): Flow<Int>
    suspend fun getTotalDistanceInMeters(): Flow<Int>
    suspend fun getTotalAverageSpeedInKMH(): Flow<Float>
    suspend fun getTotalStepCount(): Flow<Int>
     */

    suspend fun getAllRunsSortedByDate(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByTimeInMillis(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByCaloriesBurned(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByAverageSpeed(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByDistance(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByStepCount(): Flow<Resource<List<Run>>>

    suspend fun getTotalTimeInMillis(): Flow<Resource<Long>>
    suspend fun getTotalCaloriesBurned(): Flow<Resource<Int>>
    suspend fun getTotalDistanceInMeters(): Flow<Resource<Int>>
    suspend fun getTotalAverageSpeedInKMH(): Flow<Resource<Double>>
    suspend fun getTotalStepCount(): Flow<Resource<Int>>

    /**
     * Functions fetches and saves data from/to Firebase Realtime Database
     */
    suspend fun insertRunToRemoteDB(run: Run, imageUrl: String): Flow<Resource<String>>
    suspend fun removeRunFromRemoteDB(run: Run): Flow<Resource<String>>

    suspend fun getAllRunsFromRemoteDB(): Flow<Resource<List<Run>>>

    /*
    suspend fun getTotalTimeInMillisFromRemote(): Flow<Resource<Long>>
    suspend fun getTotalCaloriesBurnedFromRemote(): Flow<Resource<Int>>
    suspend fun getTotalDistanceInMetersFromRemote(): Flow<Resource<Int>>
    suspend fun getTotalAverageSpeedInKMHFromRemote(): Flow<Resource<Double>>
    suspend fun getTotalStepCountFromRemote(): Flow<Resource<Int>>
     */

    /**
     * Functions fetches and saves data from/to Firebase Storage
     */
    suspend fun insertMapImageToRemoteDB(byteArray: ByteArray, timestamp: String): Flow<Resource<String>>
    suspend fun removeMapImageFromRemoteDB(timestamp: String): Flow<Resource<String>>
}