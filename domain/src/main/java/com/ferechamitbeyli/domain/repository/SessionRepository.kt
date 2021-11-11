package com.ferechamitbeyli.domain.repository

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    /**
     * Functions fetches identifier (uid) of the current user from FirebaseAuth and sign out
     */
    suspend fun getCurrentUserIdentifier(): Flow<Resource<String>>

    suspend fun signOut(): Flow<Resource<String>>

    /**
     * Functions fetches and saves data from/to Firebase Realtime Database
     */
    suspend fun getCurrentUserFromRemoteDB(identifier: String) : Flow<Resource<User>>
    suspend fun getUserUidFromRemoteDB() : Flow<Resource<String>>
    suspend fun getUsernameFromRemoteDB() : Flow<Resource<String>>
    suspend fun getUserEmailFromRemoteDB() : Flow<Resource<String>>
    suspend fun getUserWeightFromRemoteDB() : Flow<Resource<Double>>
    suspend fun getUserNotificationStateFromRemoteDB() : Flow<Resource<Boolean>>
    suspend fun getUserPhotoUrlFromRemoteDB() : Flow<Resource<String>>

    suspend fun updateUserChangesToRemoteDB(user: User) : Flow<Resource<String>>
    suspend fun updateUsernameToRemoteDB(username: String) : Flow<Resource<String>>
    suspend fun updateUserNotificationState(isNotificationEnabled: Boolean) : Flow<Resource<String>>
    suspend fun updateUserWeightToRemoteDB(weight: Double) : Flow<Resource<String>>


    /**
     * Functions fetches and saves data from/to Jetpack Datastore
     */
    suspend fun getFirstUseState(): Flow<Resource<Boolean>>
    suspend fun cacheFirstUseState(isFirstTime: Boolean)

    suspend fun getInitialSetupState(): Flow<Resource<Boolean>>
    suspend fun cacheInitialSetupState(isInitialSetupDone: Boolean)

    suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userWeight: Double,
        userNotificationEnabled: Boolean,
        userPhotoUrl: String
    )

    suspend fun cacheUsername(username: String)
    suspend fun cacheUserNotificationState(isNotificationEnabled: Boolean)
    suspend fun cacheUserWeight(weight: Double)

    suspend fun getUserUid(): Flow<Resource<String>>
    suspend fun getUsername(): Flow<Resource<String>>
    suspend fun getUserEmail(): Flow<Resource<String>>
    suspend fun getUserWeight() : Flow<Resource<Double>>
    suspend fun getUserNotificationState() : Flow<Resource<Boolean>>
    suspend fun getUserPhotoUrl(): Flow<Resource<String>>
}