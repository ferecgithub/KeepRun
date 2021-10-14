package com.ferechamitbeyli.keeprun.framework.model.local.db

import android.content.Context
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.RunDataSource
import com.ferechamitbeyli.keeprun.framework.model.local.entities.RunEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RoomRunDataSource @Inject constructor(
    @ApplicationContext context: Context,
    private val runDao: RunDao
) : RunDataSource {
    override suspend fun insert(run: Run) = runDao.insertRunEntity(RunEntity.fromRun(run))

    override suspend fun remove(run: Run) = runDao.removeRunEntity(RunEntity.fromRun(run))

    override suspend fun getAllRunsSortedByDate(): List<Run>? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRunsSortedByTimeInMillis(): List<Run>? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRunsSortedByCaloriesBurned(): List<Run>? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRunsSortedByAverageSpeed(): List<Run>? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRunsSortedByDistance(): List<Run>? {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalTimeInMillis(): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalCaloriesBurned(): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalDistanceInMeters(): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalAverageSpeedInKMH(): Float? {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalStepCount(): Int? {
        TODO("Not yet implemented")
    }


}