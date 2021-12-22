package com.ferechamitbeyli.domain.repository

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.Run
import kotlinx.coroutines.flow.Flow

interface RunRepository {

    /**
     * Functions fetches and saves data from/to Room Database
     */
    suspend fun insertRunToDB(run: Run): Long
    suspend fun insertMultipleRunsToDB(vararg run: Run): List<Long>
    suspend fun removeRunFromDB(run: Run)
    suspend fun removeAllRunsFromDB()

    suspend fun getAllRunsSortedByDate(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByTimeInMillis(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByCaloriesBurned(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByAverageSpeed(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByDistance(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByStepCount(): Flow<Resource<List<Run>>>

    suspend fun getTotalTimeInMillisBetween(startDate: Long, endDate: Long): Flow<Resource<Long?>>
    suspend fun getTotalCaloriesBurnedBetween(startDate: Long, endDate: Long): Flow<Resource<Int?>>
    suspend fun getTotalDistanceInMetersBetween(startDate: Long, endDate: Long): Flow<Resource<Int?>>
    suspend fun getTotalAverageSpeedInKMHBetween(startDate: Long, endDate: Long): Flow<Resource<Double?>>
    suspend fun getTotalStepCountBetween(startDate: Long, endDate: Long): Flow<Resource<Int?>>

    /**
     * Functions fetches and saves data from/to Firebase Realtime Database
     */
    suspend fun insertRunToRemoteDB(run: Run, imageUrl: String): Flow<Resource<String>>
    suspend fun removeRunFromRemoteDB(run: Run): Flow<Resource<String>>
    suspend fun getAllRunsFromRemoteDB(): Flow<Resource<List<Run>>>

    /**
     * Functions fetches and saves data from/to Firebase Storage
     */
    suspend fun insertMapImageToRemoteDB(
        byteArray: ByteArray,
        timestamp: String
    ): Flow<Resource<String>>

    suspend fun removeMapImageFromRemoteDB(timestamp: String): Flow<Resource<String>>
}