package com.ferechamitbeyli.domain.repository.datasources.common

import com.ferechamitbeyli.domain.Resource
import kotlinx.coroutines.flow.Flow

interface SessionCacheDataSource {
    suspend fun getFirstUseState(): Flow<Resource<Boolean>>
    suspend fun storeFirstUseState(isFirstTime: Boolean)

    suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userPhotoUrl: String
    )
    suspend fun getUserUid(): Flow<Resource<String>>
    suspend fun getUsername(): Flow<Resource<String>>
    suspend fun getUserEmail(): Flow<Resource<String>>
    suspend fun getUserPhotoUrl(): Flow<Resource<String>>

    suspend fun getInitialSetupState(): Flow<Resource<Boolean>>
    suspend fun storeInitialSetupState(hasInitialSetupDone: Boolean)
}