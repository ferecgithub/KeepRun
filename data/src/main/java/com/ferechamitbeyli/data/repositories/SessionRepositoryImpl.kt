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

    /**
     * Functions for fetching user identifier (uid) from FirebaseAuth and sign out
     */
    override suspend fun getCurrentUserIdentifier(): Flow<Resource<String>> =
        sessionRemoteDataSource.getCurrentUserIdentifier()

    override suspend fun signOut(): Flow<Resource<String>> = sessionRemoteDataSource.signOut()

    /**
     * Functions for fetching and saving from/to Firebase Realtime Database
     */
    override suspend fun getCurrentUserFromRemoteDB(identifier: String): Flow<Resource<User>> =
        sessionRemoteDataSource.getCurrentUserFromRemoteDB(identifier)

    override suspend fun getUserUidFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUserUidFromRemoteDB()

    override suspend fun getUsernameFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUsernameFromRemoteDB()

    override suspend fun getUserNotificationStateFromRemoteDB(): Flow<Resource<Boolean>> =
        sessionRemoteDataSource.getUserNotificationStateFromRemoteDB()

    override suspend fun getUserPhotoUrlFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUserPhotoUrlFromRemoteDB()

    override suspend fun updateUserChangesToRemoteDB(user: User): Flow<Resource<String>> =
        sessionRemoteDataSource.updateUserChangesToRemoteDB(user)

    override suspend fun updateUsernameToRemoteDB(username: String): Flow<Resource<String>> =
        sessionRemoteDataSource.updateUsernameToRemoteDB(username)

    override suspend fun updateUserNotificationState(isNotificationEnabled: Boolean): Flow<Resource<String>> =
        sessionRemoteDataSource.updateUserNotificationState(isNotificationEnabled)

    /**
     * Functions for fetching and saving from/to Jetpack DataStore
     */
    override suspend fun getFirstUseState(): Flow<Resource<Boolean>> =
        sessionCacheDataSource.getFirstUseState()

    override suspend fun cacheFirstUseState(isFirstTime: Boolean) =
        sessionCacheDataSource.storeFirstUseState(isFirstTime)

    override suspend fun getInitialSetupState(): Flow<Resource<Boolean>> =
        sessionCacheDataSource.getInitialSetupState()

    override suspend fun cacheInitialSetupState(isInitialSetupDone: Boolean) =
        sessionCacheDataSource.storeInitialSetupState(isInitialSetupDone)

    override suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userNotificationEnabled: Boolean,
        userPhotoUrl: String
    ) = sessionCacheDataSource.cacheUserAccount(
        userUid,
        username,
        userEmail,
        userNotificationEnabled,
        userPhotoUrl
    )

    override suspend fun getUserUid(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserUid()

    override suspend fun getUsername(): Flow<Resource<String>> =
        sessionCacheDataSource.getUsername()

    override suspend fun getUserEmail(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserEmail()

    override suspend fun getUserPhotoUrl(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserPhotoUrl()
}