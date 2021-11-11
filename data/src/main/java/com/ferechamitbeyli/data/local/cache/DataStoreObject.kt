package com.ferechamitbeyli.data.local.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_FIRST_TIME_USE
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_HAS_PERMISSION
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_INITIAL_SETUP_DONE
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_NOTIFICATION_ENABLED
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USERNAME
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USER_EMAIL
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USER_PHOTO_URL
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USER_UID
import com.ferechamitbeyli.data.utils.Constants.CACHE_KEY_FOR_USER_WEIGHT
import com.ferechamitbeyli.data.utils.Constants.KEEPRUN_CACHE_NAME
import com.ferechamitbeyli.domain.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(KEEPRUN_CACHE_NAME)

@Singleton
class DataStoreObject @Inject constructor(@ApplicationContext appContext: Context) {

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
        val initialSetupStateCached = booleanPreferencesKey(CACHE_KEY_FOR_INITIAL_SETUP_DONE)
        val firstUseStateCached = booleanPreferencesKey(CACHE_KEY_FOR_FIRST_TIME_USE)
        val hasPermissionCached = booleanPreferencesKey(CACHE_KEY_FOR_HAS_PERMISSION)
    }

    /*
    suspend fun storeDetails(username: String, weight: Float, isFirstTime: Boolean) {
        keepRunDataStore.edit {
            it[usernameCached] = username
            it[weightCached] = weight
            it[firstUseStateCached] = isFirstTime
        }
    }

     */

    suspend fun cacheUserAccount(userUid: String, username: String, userEmail: String, userWeight: Double, isNotificationEnabled: Boolean, userPhotoUrl: String) {
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

    suspend fun cacheInitialSetupState(isInitialSetupDone: Boolean) {
        keepRunDataStore.edit {
            it[initialSetupStateCached] = isInitialSetupDone
        }
    }

    suspend fun cacheUserWeight(weight: Double) {
        keepRunDataStore.edit {
            it[userWeightCached] = weight
        }
    }

    suspend fun cachePermissionState(hasPermission: Boolean) {
        keepRunDataStore.edit {
            it[hasPermissionCached] = hasPermission
        }
    }

    fun getUserUid() = keepRunDataStore.data.map {
        Resource.Success(it[userUidCached] ?: "")
    }.catch {
        Resource.Error(it.toString(),null)
    }

    fun getUsername() = keepRunDataStore.data.map {
        Resource.Success(it[usernameCached] ?: "")
    }.catch {
        Resource.Error(it.toString(),null)
    }

    fun getUserEmail() = keepRunDataStore.data.map {
        Resource.Success(it[userEmailCached] ?: "")
    }.catch {
        Resource.Error(it.toString(),null)
    }

    fun getNotificationEnabled() = keepRunDataStore.data.map {
        Resource.Success(it[userNotificationEnableCached] ?: true)
    }.catch {
        Resource.Error(it.toString(),null)
    }

    fun getUserPhotoUrl() = keepRunDataStore.data.map {
        Resource.Success(it[userPhotoUrlCached] ?: "")
    }.catch {
        Resource.Error(it.toString(),null)
    }

    fun getUserWeight() = keepRunDataStore.data.map {
        Resource.Success(it[userWeightCached] ?: 0.0)
    }.catch {
        Resource.Error(it.toString(),null)
    }

    fun getFirstUseState() = keepRunDataStore.data.map {
        Resource.Success(it[firstUseStateCached] ?: true)
    }.catch {
        Resource.Error(it.toString(),null)
    }

    fun getInitialSetupState() = keepRunDataStore.data.map {
        Resource.Success(it[initialSetupStateCached] ?: false)
    }.catch {
        Resource.Error(it.toString(),null)
    }

    fun getHasPermission() = keepRunDataStore.data.map {
        Resource.Success(it[hasPermissionCached] ?: false)
    }.catch {
        Resource.Error(it.toString(),null)
    }

}