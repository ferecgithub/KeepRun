package com.ferechamitbeyli.domain.repository.datasources.common

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface SessionRemoteDataSource {
    suspend fun getCurrentUserIdentifier() : Flow<Resource<String>>
    suspend fun getCurrentUserFromRemoteDB(identifier: String) : Flow<Resource<User>>
    suspend fun getUserUidFromRemoteDB() : Flow<Resource<String>>
    suspend fun getUserEmailFromRemoteDB() : Flow<Resource<String>>
    suspend fun getUserWeightFromRemoteDB() : Flow<Resource<Double>>
    suspend fun getUsernameFromRemoteDB() : Flow<Resource<String>>
    suspend fun getUserNotificationStateFromRemoteDB() : Flow<Resource<Boolean>>
    suspend fun getUserPhotoUrlFromRemoteDB() : Flow<Resource<String>>

    suspend fun updateUserChangesToRemoteDB(user: User) : Flow<Resource<String>>
    suspend fun updateUsernameToRemoteDB(username: String) : Flow<Resource<String>>
    suspend fun updateUserNotificationState(isNotificationEnabled: Boolean) : Flow<Resource<String>>
    suspend fun updateUserWeightToRemoteDB(weight: Double) : Flow<Resource<String>>

    suspend fun signOut(): Flow<Resource<String>>
}