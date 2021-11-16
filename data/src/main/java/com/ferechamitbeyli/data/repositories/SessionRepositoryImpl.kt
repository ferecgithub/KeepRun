package com.ferechamitbeyli.data.repositories

import com.ferechamitbeyli.data.remote.entities.mappers.UserDtoMapper
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.SessionRepository
import com.ferechamitbeyli.domain.repository.datasources.common.SessionCacheDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sessionCacheDataSource: SessionCacheDataSource,
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val coroutineDispatchers: CoroutineDispatchers
) : SessionRepository {

    /**
     * Functions for fetching user identifier (uid) from FirebaseAuth and sign out
     */
    override suspend fun getCurrentUserIdentifier(): Flow<Resource<String>> =
        sessionRemoteDataSource.getCurrentUserIdentifier()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun signOut(): Flow<Resource<String>> = sessionRemoteDataSource.signOut()
        .catch { Resource.Error(it.message.toString(), null) }
        .flowOn(coroutineDispatchers.io())

    /**
     * Functions for fetching and saving from/to Firebase Realtime Database
     */
    override suspend fun getCurrentUserFromRemoteDB(identifier: String): Flow<Resource<User>> =
        sessionRemoteDataSource.getCurrentUserFromRemoteDB(identifier)
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserUidFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUserUidFromRemoteDB()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUsernameFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUsernameFromRemoteDB()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserEmailFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUserEmailFromRemoteDB()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserWeightFromRemoteDB(): Flow<Resource<Double>> =
        sessionRemoteDataSource.getUserWeightFromRemoteDB()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserNotificationStateFromRemoteDB(): Flow<Resource<Boolean>> =
        sessionRemoteDataSource.getUserNotificationStateFromRemoteDB()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrlFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUserPhotoUrlFromRemoteDB()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun updateUserChangesToRemoteDB(user: User): Flow<Resource<String>> =
        flow<Resource<String>> {

            sessionRemoteDataSource.updateUserChangesToRemoteDB(user).collect {
                it.data?.let { successMsg ->
                    val mappedUser = UserDtoMapper.mapFromDomainModel(user)
                    sessionCacheDataSource.cacheUserAccount(
                        firebaseAuth.currentUser?.uid.toString(),
                        mappedUser.username.toString(),
                        mappedUser.email.toString(),
                        mappedUser.weight,
                        mappedUser.isNotificationEnable,
                        mappedUser.photoUrl.toString()
                    )
                    emit(Resource.Success(successMsg))
                }
            }
        }.catch { Resource.Error(it.message.toString(), null) }.flowOn(coroutineDispatchers.io())


    override suspend fun updateUsernameToRemoteDB(username: String): Flow<Resource<String>> =
        flow<Resource<String>> {
            sessionRemoteDataSource.updateUsernameToRemoteDB(username).collect {
                it.data?.let { successMsg ->
                    sessionCacheDataSource.cacheUsername(username)
                    emit(Resource.Success(successMsg))
                }
            }
        }.catch { Resource.Error(it.message.toString(), null) }.flowOn(coroutineDispatchers.io())


    override suspend fun updateUserNotificationState(isNotificationEnabled: Boolean): Flow<Resource<String>> =
        flow<Resource<String>> {
            sessionRemoteDataSource.updateUserNotificationState(isNotificationEnabled).collect {
                it.data?.let { successMsg ->
                    sessionCacheDataSource.cacheUserNotificationState(isNotificationEnabled)
                    emit(Resource.Success(successMsg))
                }
            }
        }.catch { Resource.Error(it.message.toString(), null) }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUserWeightToRemoteDB(weight: Double): Flow<Resource<String>> =
        flow<Resource<String>> {
            sessionRemoteDataSource.updateUserWeightToRemoteDB(weight).collect {
                it.data?.let { successMsg ->
                    sessionCacheDataSource.cacheUserWeight(weight)
                    emit(Resource.Success(successMsg))
                }
            }
        }.catch { Resource.Error(it.message.toString(), null) }.flowOn(coroutineDispatchers.io())

    /**
     * Functions for fetching and saving from/to Jetpack DataStore
     */
    override suspend fun getFirstUseState(): Flow<Resource<Boolean>> =
        sessionCacheDataSource.getFirstUseState()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun cacheFirstUseState(isFirstTime: Boolean) =
        sessionCacheDataSource.storeFirstUseState(isFirstTime)

    override suspend fun getInitialSetupState(): Flow<Resource<Boolean>> =
        sessionCacheDataSource.getInitialSetupState()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun cacheInitialSetupState(isInitialSetupDone: Boolean) =
        sessionCacheDataSource.storeInitialSetupState(isInitialSetupDone)

    override suspend fun cacheUserAccount(
        userUid: String,
        username: String,
        userEmail: String,
        userWeight: Double,
        userNotificationEnabled: Boolean,
        userPhotoUrl: String
    ) = sessionCacheDataSource.cacheUserAccount(
        userUid,
        username,
        userEmail,
        userWeight,
        userNotificationEnabled,
        userPhotoUrl
    )

    override suspend fun cacheUsername(username: String) =
        sessionCacheDataSource.cacheUsername(username)

    override suspend fun cacheUserNotificationState(isNotificationEnabled: Boolean) =
        sessionCacheDataSource.cacheUserNotificationState(isNotificationEnabled)

    override suspend fun cacheUserWeight(weight: Double) =
        sessionCacheDataSource.cacheUserWeight(weight)

    override suspend fun resetCachedUser() = sessionCacheDataSource.resetCachedUser()

    override suspend fun resetCachedStates() = sessionCacheDataSource.resetCachedStates()

    override suspend fun getUserUid(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserUid()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUsername(): Flow<Resource<String>> =
        sessionCacheDataSource.getUsername()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserEmail(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserEmail()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserWeight(): Flow<Resource<Double>> =
        sessionCacheDataSource.getUserWeight()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserNotificationState(): Flow<Resource<Boolean>> =
        sessionCacheDataSource.getUserNotificationState()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrl(): Flow<Resource<String>> =
        sessionCacheDataSource.getUserPhotoUrl()
            .catch { Resource.Error(it.message.toString(), null) }
            .flowOn(coroutineDispatchers.io())
}