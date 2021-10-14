package com.ferechamitbeyli.domain.repository

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.Run
import kotlinx.coroutines.flow.Flow

interface RunDataSource {

    suspend fun insert(run: Run)
    suspend fun remove(run: Run)

    /** Get all functions **/
    suspend fun getAllRunsSortedByDate(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByTimeInMillis(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByCaloriesBurned(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByAverageSpeed(): Flow<Resource<List<Run>>>
    suspend fun getAllRunsSortedByDistance(): Flow<Resource<List<Run>>>

    /** Get total functions **/
    suspend fun getTotalTimeInMillis(): Flow<Resource<Long>>
    suspend fun getTotalCaloriesBurned(): Flow<Resource<Int>>
    suspend fun getTotalDistanceInMeters(): Flow<Resource<Int>>
    suspend fun getTotalAverageSpeedInKMH(): Flow<Resource<Float>>
    suspend fun getTotalStepCount(): Flow<Resource<Int>>

}