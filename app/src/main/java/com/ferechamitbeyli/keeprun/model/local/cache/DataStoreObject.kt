package com.ferechamitbeyli.keeprun.model.local.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ferechamitbeyli.keeprun.utils.Constants.CACHE_KEY_FOR_FIRST_TIME_USE
import com.ferechamitbeyli.keeprun.utils.Constants.CACHE_KEY_FOR_HAS_PERMISSION
import com.ferechamitbeyli.keeprun.utils.Constants.CACHE_KEY_FOR_USERNAME
import com.ferechamitbeyli.keeprun.utils.Constants.CACHE_KEY_FOR_WEIGHT
import com.ferechamitbeyli.keeprun.utils.Constants.KEEPRUN_CACHE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(KEEPRUN_CACHE_NAME)

@Singleton
class DataStoreObject @Inject constructor(@ApplicationContext appContext: Context) {

    private val keepRunDataStore = appContext.dataStore

    companion object {
        val usernameCached = stringPreferencesKey(CACHE_KEY_FOR_USERNAME)
        val weightCached = floatPreferencesKey(CACHE_KEY_FOR_WEIGHT)
        val isFirstTimeCached = booleanPreferencesKey(CACHE_KEY_FOR_FIRST_TIME_USE)
        val hasPermissionCached = booleanPreferencesKey(CACHE_KEY_FOR_HAS_PERMISSION)
    }

    suspend fun storeDetails(userName: String, weight: Float, isFirstTime: Boolean) {
        keepRunDataStore.edit {
            it[usernameCached] = userName
            it[weightCached] = weight
            it[isFirstTimeCached] = isFirstTime
        }
    }

    suspend fun storeIfFirstTimeUse(isFirstTime: Boolean) {
        keepRunDataStore.edit {
            it[isFirstTimeCached] = isFirstTime
        }
    }

    suspend fun storeHasPermission(hasPermission: Boolean) {
        keepRunDataStore.edit {
            it[hasPermissionCached] = hasPermission
        }
    }

    fun getUserName() = keepRunDataStore.data.map {
        it[usernameCached] ?: ""
    }

    fun getUserWeight() = keepRunDataStore.data.map {
        it[weightCached] ?: 80f
    }

    fun getIsFirstTime() = keepRunDataStore.data.map {
        it[isFirstTimeCached] ?: true
    }

    fun getHasPermission() = keepRunDataStore.data.map {
        it[hasPermissionCached] ?: false
    }

}