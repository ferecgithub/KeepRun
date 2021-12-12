package com.ferechamitbeyli.data.repositories.datasources.common

import com.ferechamitbeyli.data.local.cache.DataStoreManager
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.repository.datasources.common.SessionCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SessionCacheDataSourceImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val coroutineDispatchers: CoroutineDispatchers
) : SessionCacheDataSource {

    override suspend fun getFirstUseState(): Flow<Boolean> =
        dataStoreManager.getFirstUseState()
            .flowOn(coroutineDispatchers.io())

    override suspend fun storeFirstUseState(isFirstUse: Boolean) =
        dataStoreManager.cacheFirstUseState(isFirstUse)

    override suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userWeight: Double,
        userNotificationEnabled: Boolean,
        userPhotoUrl: String
    ) = dataStoreManager.cacheUserAccount(
        userUid,
        username,
        userEmail,
        userWeight,
        userNotificationEnabled,
        userPhotoUrl
    )

    override suspend fun cacheUsername(username: String) = dataStoreManager.cacheUsername(username)

    override suspend fun cacheUserNotificationState(isNotificationEnabled: Boolean) =
        dataStoreManager.cacheUserNotificationState(isNotificationEnabled)

    override suspend fun cacheUserWeight(weight: Double) = dataStoreManager.cacheUserWeight(weight)

    override suspend fun resetCachedUser() = dataStoreManager.resetCachedUser()

    override suspend fun resetCachedStates() = dataStoreManager.resetCachedStates()

    override suspend fun getUserUid(): Flow<String> =
        dataStoreManager.getUserUid()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUsername(): Flow<String> =
        dataStoreManager.getUsername()
            .flowOn(coroutineDispatchers.io())


    override suspend fun getUserEmail(): Flow<String> =
        dataStoreManager.getUserEmail()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserWeight(): Flow<Double> =
        dataStoreManager.getUserWeight()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserNotificationState(): Flow<Boolean> =
        dataStoreManager.getNotificationEnabled()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrl(): Flow<String> =
        dataStoreManager.getUserPhotoUrl()
            .flowOn(coroutineDispatchers.io())

}