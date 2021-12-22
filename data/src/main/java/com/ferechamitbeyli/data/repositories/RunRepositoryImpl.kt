package com.ferechamitbeyli.data.repositories

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.RunRepository
import com.ferechamitbeyli.domain.repository.datasources.home.RunLocalDataSource
import com.ferechamitbeyli.domain.repository.datasources.home.RunRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RunRepositoryImpl @Inject constructor(
    private val runLocalDataSource: RunLocalDataSource,
    private val runRemoteDataSource: RunRemoteDataSource,
    private val coroutineDispatchers: CoroutineDispatchers
) : RunRepository {

    /**
     * Functions for fetching and saving from/to Jetpack Room Database
     */

    override suspend fun insertRunToDB(run: Run) = runLocalDataSource.insertToDB(run)

    override suspend fun insertMultipleRunsToDB(vararg run: Run) =
        runLocalDataSource.insertMultipleToDB(*run)

    override suspend fun removeRunFromDB(run: Run) = runLocalDataSource.removeFromDB(run)

    override suspend fun removeAllRunsFromDB() = runLocalDataSource.removeAllFromDB()

    override suspend fun getAllRunsSortedByDate(): Flow<Resource<List<Run>>> =
        runLocalDataSource.getAllRunsSortedByDate()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByTimeInMillis(): Flow<Resource<List<Run>>> =
        runLocalDataSource.getAllRunsSortedByTimeInMillis()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByCaloriesBurned(): Flow<Resource<List<Run>>> =
        runLocalDataSource.getAllRunsSortedByCaloriesBurned()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByAverageSpeed(): Flow<Resource<List<Run>>> =
        runLocalDataSource.getAllRunsSortedByAverageSpeed()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByDistance(): Flow<Resource<List<Run>>> =
        runLocalDataSource.getAllRunsSortedByDistance()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsSortedByStepCount(): Flow<Resource<List<Run>>> =
        runLocalDataSource.getAllRunsSortedByStepCount()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getTotalTimeInMillisBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Long?>> =
        runLocalDataSource.getTotalTimeInMillisBetween(startDate, endDate)
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getTotalCaloriesBurnedBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Int?>> =
        runLocalDataSource.getTotalCaloriesBurnedBetween(startDate, endDate)
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getTotalDistanceInMetersBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Int?>> =
        runLocalDataSource.getTotalDistanceInMetersBetween(startDate, endDate)
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getTotalAverageSpeedInKMHBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Double?>> =
        runLocalDataSource.getTotalAverageSpeedInKMHBetween(startDate, endDate)
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getTotalStepCountBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Resource<Int?>> =
        runLocalDataSource.getTotalStepCountBetween(startDate, endDate)
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    /**
     * Functions for fetching and saving from/to Firebase Realtime Database
     */

    override suspend fun insertRunToRemoteDB(run: Run, imageUrl: String): Flow<Resource<String>> =
        runRemoteDataSource.insertRunToRemoteDB(run, imageUrl)
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun removeRunFromRemoteDB(run: Run): Flow<Resource<String>> =
        runRemoteDataSource.removeRunFromRemoteDB(run)
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getAllRunsFromRemoteDB(): Flow<Resource<List<Run>>> =
        runRemoteDataSource.getAllRunsFromRemoteDB()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())


    /**
     * Functions for fetching and saving from/to Firebase Storage
     */

    override suspend fun insertMapImageToRemoteDB(
        byteArray: ByteArray,
        timestamp: String
    ): Flow<Resource<String>> = runRemoteDataSource.insertMapImageToRemoteDB(byteArray, timestamp)
        .catch { Resource.Error(it.message.toString(), null) }
        .flowOn(coroutineDispatchers.io())

    override suspend fun removeMapImageFromRemoteDB(timestamp: String): Flow<Resource<String>> =
        runRemoteDataSource.removeMapImageFromRemoteDB(timestamp)
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())
}