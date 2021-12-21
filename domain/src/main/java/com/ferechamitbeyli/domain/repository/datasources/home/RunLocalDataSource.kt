package com.ferechamitbeyli.domain.repository.datasources.home

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.Run
import kotlinx.coroutines.flow.Flow

interface RunLocalDataSource {
    suspend fun insertToDB(run: Run) : Long
    suspend fun insertMultipleToDB(vararg run: Run) : List<Long>
    suspend fun removeFromDB(run: Run)
    suspend fun removeAllFromDB()

    /** Get all functions **/
    suspend fun getAllRunsSortedByDate(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByTimeInMillis(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByCaloriesBurned(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByAverageSpeed(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByDistance(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByStepCount(): Flow<Resource<List<Run>>>

    /** Get total functions **/
    suspend fun getTotalTimeInMillisBetween(startDate: Long, endDate: Long): Flow<Resource<Long>>
    suspend fun getTotalCaloriesBurnedBetween(startDate: Long, endDate: Long): Flow<Resource<Int>>
    suspend fun getTotalDistanceInMetersBetween(startDate: Long, endDate: Long): Flow<Resource<Int>>
    suspend fun getTotalAverageSpeedInKMHBetween(startDate: Long, endDate: Long): Flow<Resource<Double>>
    suspend fun getTotalStepCountBetween(startDate: Long, endDate: Long): Flow<Resource<Int>>

    suspend fun getTotalTimeInMillis(): Flow<Resource<Long>>
    suspend fun getTotalCaloriesBurned(): Flow<Resource<Int>>
    suspend fun getTotalDistanceInMeters(): Flow<Resource<Int>>
    suspend fun getTotalAverageSpeedInKMH(): Flow<Resource<Double>>
    suspend fun getTotalStepCount(): Flow<Resource<Int>>
}