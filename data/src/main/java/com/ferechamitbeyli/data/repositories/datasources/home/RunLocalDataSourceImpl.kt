package com.ferechamitbeyli.data.repositories.datasources.home

import com.ferechamitbeyli.data.local.db.RunDao
import com.ferechamitbeyli.data.local.entities.RunEntity
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.datasources.home.RunLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RunLocalDataSourceImpl @Inject constructor(
    private val runDao: RunDao,
    private val runEntityMapper: DomainMapper<RunEntity, Run>,
    private val coroutineDispatchers: CoroutineDispatchers
) : RunLocalDataSource {

    override suspend fun insertToDB(run: Run) =
        runDao.insert(runEntityMapper.mapFromDomainModel(run))

    override suspend fun insertMultipleToDB(vararg run: Run) =
        runDao.insertMultiple(*runEntityMapper.mapFromDomainModelList(run.toList()).toTypedArray())

    override suspend fun removeFromDB(run: Run) {
        runDao.remove(runEntityMapper.mapFromDomainModel(run))
    }

    override suspend fun removeAllFromDB() {
        runDao.removeAll()
    }

    override suspend fun getAllRunsSortedByDate(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByDate()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByTimeInMillis(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByTimeInMillis()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByCaloriesBurned(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByCaloriesBurned()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByAverageSpeed(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByAverageSpeed()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByDistance(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByDistance()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByStepCount(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByStepCount()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getTotalTimeInMillisBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Long?>> =
        runDao.getTotalTimeInMillisBetween(startDate, endDate).map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getTotalCaloriesBurnedBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Int?>> =
        runDao.getTotalCaloriesBurnedBetween(startDate, endDate).map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getTotalDistanceInMetersBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Int?>> =
        runDao.getTotalDistanceInMetersBetween(startDate, endDate).map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getTotalAverageSpeedInKMHBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Double?>> =
        runDao.getTotalAverageSpeedInKMHBetween(startDate, endDate).map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getTotalStepCountBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Int?>> =
        runDao.getTotalStepCountBetween(startDate, endDate).map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

}