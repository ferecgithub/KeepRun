package com.ferechamitbeyli.domain.repository.datasources.common

import kotlinx.coroutines.flow.Flow

interface SessionCacheDataSource {

    suspend fun getFirstUseState(): Flow<Boolean>
    suspend fun storeFirstUseState(isFirstUse: Boolean)

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

    suspend fun resetCachedUser()
    suspend fun resetCachedStates()

    suspend fun getUserUid(): Flow<String>
    suspend fun getUsername(): Flow<String>
    suspend fun getUserEmail(): Flow<String>
    suspend fun getUserWeight(): Flow<Double>
    suspend fun getUserNotificationState(): Flow<Boolean>
    suspend fun getUserPhotoUrl(): Flow<String>

    /**
     * Permissions
     */

    suspend fun cacheFineLocationPermissionState(hasPermission: Boolean)
    suspend fun cacheActivityRecognitionPermissionState(hasPermission: Boolean)

    suspend fun getFineLocationPermissionState(): Flow<Boolean>
    suspend fun getActivityRecognitionPermissionState(): Flow<Boolean>

}