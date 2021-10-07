package com.ferechamitbeyli.core.repository

import com.ferechamitbeyli.core.data.Run

interface RunDataSource {

    suspend fun insert(run: Run)
    suspend fun remove(run: Run)

    /** Get all functions **/
    suspend fun getAllRunsSortedByDate(): List<Run>?
    suspend fun getAllRunsSortedByTimeInMillis(): List<Run>?
    suspend fun getAllRunsSortedByCaloriesBurned(): List<Run>?
    suspend fun getAllRunsSortedByAverageSpeed(): List<Run>?
    suspend fun getAllRunsSortedByDistance(): List<Run>?

    /** Get total functions **/
    suspend fun getTotalTimeInMillis(): Long?
    suspend fun getTotalCaloriesBurned(): Int?
    suspend fun getTotalDistanceInMeters(): Int?
    suspend fun getTotalAverageSpeedInKMH(): Float?
    suspend fun getTotalStepCount(): Int?

}