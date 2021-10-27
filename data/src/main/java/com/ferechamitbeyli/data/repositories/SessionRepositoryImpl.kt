package com.ferechamitbeyli.data.repositories

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.SessionRepository
import com.ferechamitbeyli.domain.repository.datasources.common.SessionCacheDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionCacheDataSource: SessionCacheDataSource,
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val coroutineDispatchers: CoroutineDispatchers
) : SessionRepository {

    override suspend fun getFirstUseState(): Flow<Resource<Boolean>> =
        sessionCacheDataSource.getFirstUseState().flowOn(coroutineDispatchers.io())

    override suspend fun storeFirstUseState(isFirstTime: Boolean) =
        sessionCacheDataSource.storeFirstUseState(isFirstTime)

    override suspend fun getInitialSetupState(): Flow<Resource<Boolean>> =
        sessionCacheDataSource.getInitialSetupState().flowOn(coroutineDispatchers.io())

    override suspend fun storeInitialSetupState(isInitialSetupDone: Boolean) =
        sessionCacheDataSource.storeInitialSetupState(isInitialSetupDone)

    override suspend fun getCurrentUser(): Flow<Resource<User>> =
        sessionRemoteDataSource.getCurrentUser().flowOn(coroutineDispatchers.io())

    override suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userPhotoUrl: String
    ) = sessionCacheDataSource.cacheUserAccount(userUid, username, userEmail, userPhotoUrl)

    override suspend fun getUserUid(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserUid().flowOn(coroutineDispatchers.io())

    override suspend fun getUsername(): Flow<Resource<String>> =
        sessionCacheDataSource.getUsername().flowOn(coroutineDispatchers.io())

    override suspend fun getUserEmail(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserEmail().flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrl(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserPhotoUrl().flowOn(coroutineDispatchers.io())
}