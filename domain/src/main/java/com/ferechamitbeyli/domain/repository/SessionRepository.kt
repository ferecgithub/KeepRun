package com.ferechamitbeyli.domain.repository

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun getFirstUseState(): Flow<Resource<Boolean>>
    suspend fun storeFirstUseState(isFirstTime: Boolean)

    suspend fun getCurrentUser(): Flow<Resource<User>>
    suspend fun signOut(): Flow<Resource<String>>

    suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userNotificationEnabled: Boolean,
        userPhotoUrl: String
    )
    suspend fun getUserUid(): Flow<Resource<String>>
    suspend fun getUsername(): Flow<Resource<String>>
    suspend fun getUserEmail(): Flow<Resource<String>>
    suspend fun getUserPhotoUrl(): Flow<Resource<String>>

    suspend fun getInitialSetupState(): Flow<Resource<Boolean>>
    suspend fun storeInitialSetupState(isInitialSetupDone: Boolean)
}