package com.ferechamitbeyli.core.repository

import com.ferechamitbeyli.core.data.Run

class RunRepository(
    private val dataSource: RunDataSource
) {
    suspend fun insert(run: Run) = dataSource.insert(run)
    suspend fun remove(run: Run) = dataSource.remove(run)

    suspend fun getAllRunsSortedByDate() = dataSource.getAllRunsSortedByDate()
    suspend fun getAllRunsSortedByTimeInMillis() = dataSource.getAllRunsSortedByTimeInMillis()
    suspend fun getAllRunsSortedByCaloriesBurned() = dataSource.getAllRunsSortedByCaloriesBurned()
    suspend fun getAllRunsSortedByAverageSpeed() = dataSource.getAllRunsSortedByAverageSpeed()
    suspend fun getAllRunsSortedByDistance() = dataSource.getAllRunsSortedByDistance()

    suspend fun getTotalTimeInMillis() = dataSource.getTotalTimeInMillis()
    suspend fun getTotalCaloriesBurned() = dataSource.getTotalCaloriesBurned()
    suspend fun getTotalDistanceInMeters() = dataSource.getTotalDistanceInMeters()
    suspend fun getTotalAverageSpeedInKMH() = dataSource.getTotalAverageSpeedInKMH()
    suspend fun getTotalStepCount() = dataSource.getTotalStepCount()

}