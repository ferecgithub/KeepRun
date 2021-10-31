package com.ferechamitbeyli.data.repositories

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.SessionRepository
import com.ferechamitbeyli.domain.repository.datasources.common.SessionCacheDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionCacheDataSource: SessionCacheDataSource,
    private val sessionRemoteDataSource: SessionRemoteDataSource
) : SessionRepository {

    override suspend fun getFirstUseState(): Flow<Resource<Boolean>> =
        sessionCacheDataSource.getFirstUseState()

    override suspend fun storeFirstUseState(isFirstTime: Boolean) =
        sessionCacheDataSource.storeFirstUseState(isFirstTime)

    override suspend fun getInitialSetupState(): Flow<Resource<Boolean>> =
        sessionCacheDataSource.getInitialSetupState()

    override suspend fun storeInitialSetupState(isInitialSetupDone: Boolean) =
        sessionCacheDataSource.storeInitialSetupState(isInitialSetupDone)

    override suspend fun getCurrentUser(): Flow<Resource<User>> =
        sessionRemoteDataSource.getCurrentUser()

    override suspend fun signOut(): Flow<Resource<String>> = sessionRemoteDataSource.signOut()

    override suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userNotificationEnabled: Boolean,
        userPhotoUrl: String
    ) = sessionCacheDataSource.cacheUserAccount(userUid, username, userEmail, userNotificationEnabled, userPhotoUrl)

    override suspend fun getUserUid(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserUid()

    override suspend fun getUsername(): Flow<Resource<String>> =
        sessionCacheDataSource.getUsername()

    override suspend fun getUserEmail(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserEmail()

    override suspend fun getUserPhotoUrl(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserPhotoUrl()
}