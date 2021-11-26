package com.ferechamitbeyli.data.local.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_FIRST_TIME_USE
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_HAS_ACTIVITY_RECOGNITION_PERMISSION
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_HAS_FINE_LOCATION_PERMISSION
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_NOTIFICATION_ENABLED
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USERNAME
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USER_EMAIL
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USER_PHOTO_URL
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USER_UID
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USER_WEIGHT
import com.ferechamitbeyli.data.utils.Constants.KEEPRUN_CACHE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(KEEPRUN_CACHE_NAME)

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val keepRunDataStore = appContext.dataStore

    companion object {
        /** User Account Information **/
        val userUidCached = stringPreferencesKey(CACHE_KEY_FOR_USER_UID)
        val usernameCached = stringPreferencesKey(CACHE_KEY_FOR_USERNAME)
        val userEmailCached = stringPreferencesKey(CACHE_KEY_FOR_USER_EMAIL)
        val userWeightCached = doublePreferencesKey(CACHE_KEY_FOR_USER_WEIGHT)
        val userNotificationEnableCached = booleanPreferencesKey(CACHE_KEY_FOR_NOTIFICATION_ENABLED)
        val userPhotoUrlCached = stringPreferencesKey(CACHE_KEY_FOR_USER_PHOTO_URL)

        /** Miscellaneous Information **/
        val firstUseStateCached = booleanPreferencesKey(CACHE_KEY_FOR_FIRST_TIME_USE)
        val hasFineLocationPermissionCached =
            booleanPreferencesKey(CACHE_KEY_FOR_HAS_FINE_LOCATION_PERMISSION)
        val hasActivityRecognitionPermissionCached =
            booleanPreferencesKey(CACHE_KEY_FOR_HAS_ACTIVITY_RECOGNITION_PERMISSION)
    }

    suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userWeight: Double,
        isNotificationEnabled: Boolean,
        userPhotoUrl: String
    ) {
        keepRunDataStore.edit {
            it[userUidCached] = userUid
            it[usernameCached] = username
            it[userEmailCached] = userEmail
            it[userWeightCached] = userWeight
            it[userNotificationEnableCached] = isNotificationEnabled
            it[userPhotoUrlCached] = userPhotoUrl
        }
    }

    suspend fun cacheUsername(username: String) {
        keepRunDataStore.edit {
            it[usernameCached] = username
        }
    }

    suspend fun cacheUserNotificationState(isNotificationEnabled: Boolean) {
        keepRunDataStore.edit {
            it[userNotificationEnableCached] = isNotificationEnabled
        }
    }

    suspend fun cacheFirstUseState(isFirstTime: Boolean) {
        keepRunDataStore.edit {
            it[firstUseStateCached] = isFirstTime
        }
    }

    suspend fun cacheUserWeight(weight: Double) {
        keepRunDataStore.edit {
            it[userWeightCached] = weight
        }
    }

    suspend fun cacheFineLocationPermissionState(hasPermission: Boolean) {
        keepRunDataStore.edit {
            it[hasFineLocationPermissionCached] = hasPermission
        }
    }

    suspend fun cacheActivityRecognitionPermissionState(hasPermission: Boolean) {
        keepRunDataStore.edit {
            it[hasActivityRecognitionPermissionCached] = hasPermission
        }
    }

    suspend fun resetCachedUser() {
        keepRunDataStore.edit {
            it[userUidCached] = ""
            it[usernameCached] = ""
            it[userEmailCached] = ""
            it[userWeightCached] = 0.0
            it[userNotificationEnableCached] = true
            it[userPhotoUrlCached] = ""
        }
    }

    suspend fun resetCachedStates() {
        keepRunDataStore.edit {
            it[firstUseStateCached] = true
            it[hasFineLocationPermissionCached] = false
            it[hasActivityRecognitionPermissionCached] = false
        }
    }

    fun getUserUid() = keepRunDataStore.data.map {
        it[userUidCached] ?: ""
    }

    fun getUsername() = keepRunDataStore.data.map {
        it[usernameCached] ?: ""
    }

    fun getUserEmail() = keepRunDataStore.data.map {
        it[userEmailCached] ?: ""
    }

    fun getNotificationEnabled() = keepRunDataStore.data.map {
        it[userNotificationEnableCached] ?: true
    }

    fun getUserPhotoUrl() = keepRunDataStore.data.map {
        it[userPhotoUrlCached] ?: ""
    }

    fun getUserWeight() = keepRunDataStore.data.map {
        it[userWeightCached] ?: 0.0
    }

    fun getFirstUseState() = keepRunDataStore.data.map {
        it[firstUseStateCached] ?: true
    }

    fun getFineLocationPermissionState() = keepRunDataStore.data.map {
        it[hasFineLocationPermissionCached] ?: false
    }

    fun getActivityRecognitionPermissionState() = keepRunDataStore.data.map {
        it[hasActivityRecognitionPermissionCached] ?: false
    }

}