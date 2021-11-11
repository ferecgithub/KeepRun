package com.ferechamitbeyli.data.repositories.datasources.common

import com.ferechamitbeyli.data.local.cache.DataStoreObject
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.repository.datasources.common.SessionCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SessionCacheDataSourceImpl @Inject constructor(
    private val dataStoreObject: DataStoreObject,
    private val coroutineDispatchers: CoroutineDispatchers
) : SessionCacheDataSource {

    override suspend fun getFirstUseState(): Flow<Resource<Boolean>> =
        dataStoreObject.getFirstUseState().flowOn(coroutineDispatchers.io())

    override suspend fun storeFirstUseState(isFirstTime: Boolean) =
        dataStoreObject.cacheFirstUseState(isFirstTime)

    override suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userWeight: Double,
        userNotificationEnabled: Boolean,
        userPhotoUrl: String
    ) = dataStoreObject.cacheUserAccount(
        userUid,
        username,
        userEmail,
        userWeight,
        userNotificationEnabled,
        userPhotoUrl
    )

    override suspend fun cacheUsername(username: String) = dataStoreObject.cacheUsername(username)

    override suspend fun cacheUserNotificationState(isNotificationEnabled: Boolean) =
        dataStoreObject.cacheUserNotificationState(isNotificationEnabled)

    override suspend fun cacheUserWeight(weight: Double) = dataStoreObject.cacheUserWeight(weight)

    override suspend fun getUserUid(): Flow<Resource<String>> =
        dataStoreObject.getUserUid().flowOn(coroutineDispatchers.io())

    override suspend fun getUsername(): Flow<Resource<String>> =
        dataStoreObject.getUsername().flowOn(coroutineDispatchers.io())

    override suspend fun getUserEmail(): Flow<Resource<String>> =
        dataStoreObject.getUserEmail().flowOn(coroutineDispatchers.io())

    override suspend fun getUserWeight(): Flow<Resource<Double>> =
        dataStoreObject.getUserWeight().flowOn(coroutineDispatchers.io())

    override suspend fun getUserNotificationState(): Flow<Resource<Boolean>> =
        dataStoreObject.getNotificationEnabled()

    override suspend fun getUserPhotoUrl(): Flow<Resource<String>> =
        dataStoreObject.getUserPhotoUrl().flowOn(coroutineDispatchers.io())

    override suspend fun getInitialSetupState(): Flow<Resource<Boolean>> =
        dataStoreObject.getInitialSetupState().flowOn(coroutineDispatchers.io())

    override suspend fun storeInitialSetupState(hasInitialSetupDone: Boolean) =
        dataStoreObject.cacheInitialSetupState(hasInitialSetupDone)

}