package com.ferechamitbeyli.data.repositories.datasources.home

import com.ferechamitbeyli.data.local.db.RunDao
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.datasources.home.RunLocalDataSource
import com.ferechamitbeyli.data.local.entities.RunEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RunLocalDataSourceImpl @Inject constructor(
    private val runDao: RunDao
) : RunLocalDataSource {
    override suspend fun insertToDB(run: Run) {
        runDao.insert(RunEntity.fromRun(run))
    }

    override suspend fun removeFromDB(run: Run) {
        runDao.remove(RunEntity.fromRun(run))
    }

    override suspend fun getAllRunsSortedByDate(): Flow<List<Run>> =
        runDao.getAllRunsSortedByDate().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByTimeInMillis(): Flow<List<Run>> =
        runDao.getAllRunsSortedByTimeInMillis().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByCaloriesBurned(): Flow<List<Run>> =
        runDao.getAllRunsSortedByCaloriesBurned().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByAverageSpeed(): Flow<List<Run>> =
        runDao.getAllRunsSortedByAverageSpeed().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByDistance(): Flow<List<Run>> =
        runDao.getAllRunsSortedByDistance().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getTotalTimeInMillis(): Flow<Long> = runDao.getTotalTimeInMillis()

    override suspend fun getTotalCaloriesBurned(): Flow<Int> = runDao.getTotalCaloriesBurned()

    override suspend fun getTotalDistanceInMeters(): Flow<Int> = runDao.getTotalDistanceInMeters()

    override suspend fun getTotalAverageSpeedInKMH(): Flow<Float> = runDao.getTotalAverageSpeedInKMH()

    override suspend fun getTotalStepCount(): Flow<Int> = runDao.getTotalStepCount()

    /*

    override suspend fun insert(run: Run) = runDao.insertRunEntity(RunEntity.fromRun(run))

    override suspend fun remove(run: Run) = runDao.removeRunEntity(RunEntity.fromRun(run))

     */
}