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

    override suspend fun insertToDB(run: Run) = runDao.insert(runEntityMapper.mapFromDomainModel(run))

    override suspend fun insertMultipleToDB(vararg run: Run) = runDao.insertMultiple(*runEntityMapper.mapFromDomainModelList(run.toList()).toTypedArray())

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
    //runDao.getAllRunsSortedByDate().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByTimeInMillis(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByTimeInMillis()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())
    //runDao.getAllRunsSortedByTimeInMillis().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByCaloriesBurned(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByCaloriesBurned()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())
    //runDao.getAllRunsSortedByCaloriesBurned().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByAverageSpeed(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByAverageSpeed()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())
    //runDao.getAllRunsSortedByAverageSpeed().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByDistance(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByDistance()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByStepCount(): Flow<Resource<List<Run>>> =
        runDao.getAllRunsSortedByStepCount()
            .map { list -> Resource.Success(runEntityMapper.mapToDomainModelList(list)) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())
    //runDao.getAllRunsSortedByDistance().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getTotalTimeInMillis(): Flow<Resource<Long>> =
        runDao.getTotalTimeInMillis().map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())
    //runDao.getTotalTimeInMillis()

    override suspend fun getTotalCaloriesBurned(): Flow<Resource<Int>> =
        runDao.getTotalCaloriesBurned().map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getTotalDistanceInMeters(): Flow<Resource<Int>> =
        runDao.getTotalDistanceInMeters().map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getTotalAverageSpeedInKMH(): Flow<Resource<Double>> =
        runDao.getTotalAverageSpeedInKMH().map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun getTotalStepCount(): Flow<Resource<Int>> =
        runDao.getTotalStepCount().map { Resource.Success(it) }
            .catch { Resource.Error(it.toString(), null) }.flowOn(coroutineDispatchers.io())

    /*
    override suspend fun getAllRunsSortedByDate(): Flow<List<Run>> =
        runDao.getAllRunsSortedByDate().map { list -> RunEntityMapper.mapToDomainModelList(list) }
        //runDao.getAllRunsSortedByDate().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByTimeInMillis(): Flow<List<Run>> =
        runDao.getAllRunsSortedByTimeInMillis().map { list -> RunEntityMapper.mapToDomainModelList(list) }
        //runDao.getAllRunsSortedByTimeInMillis().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByCaloriesBurned(): Flow<List<Run>> =
        runDao.getAllRunsSortedByCaloriesBurned().map { list -> RunEntityMapper.mapToDomainModelList(list) }
        //runDao.getAllRunsSortedByCaloriesBurned().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByAverageSpeed(): Flow<List<Run>> =
        runDao.getAllRunsSortedByAverageSpeed().map { list -> RunEntityMapper.mapToDomainModelList(list) }
        //runDao.getAllRunsSortedByAverageSpeed().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getAllRunsSortedByDistance(): Flow<List<Run>> =
        runDao.getAllRunsSortedByDistance().map { list -> RunEntityMapper.mapToDomainModelList(list) }
        //runDao.getAllRunsSortedByDistance().map { list -> list.map { runEntity -> runEntity.toRun() } }

    override suspend fun getTotalTimeInMillis(): Flow<Long> = runDao.getTotalTimeInMillis()

    override suspend fun getTotalCaloriesBurned(): Flow<Int> = runDao.getTotalCaloriesBurned()

    override suspend fun getTotalDistanceInMeters(): Flow<Int> = runDao.getTotalDistanceInMeters()

    override suspend fun getTotalAverageSpeedInKMH(): Flow<Double> = runDao.getTotalAverageSpeedInKMH()

    override suspend fun getTotalStepCount(): Flow<Int> = runDao.getTotalStepCount()
     */

}