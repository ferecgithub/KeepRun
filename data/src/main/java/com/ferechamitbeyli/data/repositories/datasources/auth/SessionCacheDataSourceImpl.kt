package com.ferechamitbeyli.data.repositories.datasources.auth

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
        dataStoreObject.storeFirstUseState(isFirstTime)

    override suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userPhotoUrl: String
    ) = dataStoreObject.storeUserAccount(userUid, username, userEmail, userPhotoUrl)

    override suspend fun getUserUid(): Flow<Resource<String>> =
        dataStoreObject.getUserUid().flowOn(coroutineDispatchers.io())

    override suspend fun getUsername(): Flow<Resource<String>> =
        dataStoreObject.getUsername().flowOn(coroutineDispatchers.io())

    override suspend fun getUserEmail(): Flow<Resource<String>> =
        dataStoreObject.getUserEmail().flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrl(): Flow<Resource<String>> =
        dataStoreObject.getUserPhotoUrl().flowOn(coroutineDispatchers.io())

    override suspend fun getInitialSetupState(): Flow<Resource<Boolean>> =
        dataStoreObject.getInitialSetupState().flowOn(coroutineDispatchers.io())

    override suspend fun storeInitialSetupState(hasInitialSetupDone: Boolean) =
        dataStoreObject.storeInitialSetupState(hasInitialSetupDone)

}