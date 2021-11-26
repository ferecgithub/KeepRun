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
        flow<Resource<String>> {

            sessionRemoteDataSource.getCurrentUserIdentifier()
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { identifierResponse ->

                    emit(Resource.Loading())

                    when (identifierResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(identifierResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            emit(Resource.Success(identifierResponse.data.toString()))
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
        }

    override suspend fun updateUserPassword(password: String): Flow<Resource<String>> =
        flow<Resource<String>> {
            sessionRemoteDataSource.updateUserPassword(password)
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { passwordResponse ->

                    emit(Resource.Loading())

                    when (passwordResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(passwordResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            emit(Resource.Success(passwordResponse.data.toString()))
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())


    override suspend fun signOut(): Flow<Resource<String>> = flow<Resource<String>> {

        sessionRemoteDataSource.signOut()
            .catch { emit(Resource.Error(it.message.toString(), null)) }
            .flowOn(coroutineDispatchers.io())
            .collect { signOutResponse ->

                emit(Resource.Loading())

                when (signOutResponse) {
                    is Resource.Error -> {
                        emit(Resource.Error(signOutResponse.message.toString()))
                    }
                    is Resource.Success -> {

                    }
                    else -> {
                        /** NO-OP **/
                    }
                }

            }
    }


    /**
     * Functions for fetching and saving from/to Firebase Realtime Database
     */
    override suspend fun getCurrentUserFromRemoteDB(): Flow<Resource<User>> = flow<Resource<User>> {

        sessionRemoteDataSource.getCurrentUserFromRemoteDB()
            .catch { emit(Resource.Error(it.message.toString(), null)) }
            .flowOn(coroutineDispatchers.io())
            .collect { userResponse ->

                emit(Resource.Loading())

                when (userResponse) {
                    is Resource.Error -> {
                        emit(Resource.Error(userResponse.message.toString()))
                    }
                    is Resource.Success -> {
                        userResponse.data?.let {
                            val userDto = UserDtoMapper.mapFromDomainModel(it)
                            sessionCacheDataSource.cacheUserAccount(
                                userUid = firebaseAuth.currentUser?.uid.toString(),
                                username = userDto.username.toString(),
                                userEmail = userDto.email.toString(),
                                userWeight = userDto.weight,
                                userNotificationEnabled = userDto.isNotificationEnable,
                                userPhotoUrl = userDto.photoUrl.toString()
                            ).also {
                                emit(Resource.Success(UserDtoMapper.mapToDomainModel(userDto)))
                            }
                        }
                    }
                    else -> {
                        /** NO-OP **/
                    }
                }
            }

    }


    override suspend fun getUserUidFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUserUidFromRemoteDB()
            .catch { emit(Resource.Error(it.message.toString(), null)) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUsernameFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {
            sessionRemoteDataSource.getUsernameFromRemoteDB()
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { usernameResponse ->

                    emit(Resource.Loading())

                    when (usernameResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(usernameResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            usernameResponse.data?.let { username ->
                                sessionCacheDataSource.cacheUsername(username).also {
                                    emit(Resource.Success(username))
                                }
                            }
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
        }


    override suspend fun getUserEmailFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUserEmailFromRemoteDB()
            .catch { emit(Resource.Error(it.message.toString(), null)) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrlFromRemoteDB(): Flow<Resource<String>> =
        sessionRemoteDataSource.getUserPhotoUrlFromRemoteDB()
            .catch { emit(Resource.Error(it.message.toString(), null)) }
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserWeightFromRemoteDB(): Flow<Resource<Double>> =
        flow<Resource<Double>> {
            sessionRemoteDataSource.getUserWeightFromRemoteDB()
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { weightResponse ->

                    emit(Resource.Loading())

                    when (weightResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(weightResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            weightResponse.data?.let { weight ->
                                sessionCacheDataSource.cacheUserWeight(weight).also {
                                    emit(Resource.Success(weight))
                                }
                            }
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
        }

    override suspend fun getUserNotificationStateFromRemoteDB(): Flow<Resource<Boolean>> =
        flow<Resource<Boolean>> {
            sessionRemoteDataSource.getUserNotificationStateFromRemoteDB()
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { notificationStateResponse ->

                    emit(Resource.Loading())

                    when (notificationStateResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(notificationStateResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            notificationStateResponse.data?.let { notificationState ->
                                sessionCacheDataSource.cacheUserNotificationState(notificationState)
                                    .also {
                                        emit(Resource.Success(notificationState))
                                    }
                            }
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }

                }
        }

    override suspend fun updateUserChangesToRemoteDB(user: User): Flow<Resource<String>> =
        flow<Resource<String>> {

            sessionRemoteDataSource.updateUserChangesToRemoteDB(user)
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { updateResponse ->

                    emit(Resource.Loading())

                    when (updateResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(updateResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            updateResponse.data?.let { successMsg ->
                                val mappedUser = UserDtoMapper.mapFromDomainModel(user)
                                sessionCacheDataSource.cacheUserAccount(
                                    firebaseAuth.currentUser?.uid.toString(),
                                    mappedUser.username.toString(),
                                    mappedUser.email.toString(),
                                    mappedUser.weight,
                                    mappedUser.isNotificationEnable,
                                    mappedUser.photoUrl.toString()
                                ).also {
                                    emit(Resource.Success(successMsg))
                                }

                            }
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
        }


    override suspend fun updateUsernameToRemoteDB(username: String): Flow<Resource<String>> =
        flow<Resource<String>> {
            sessionRemoteDataSource.updateUsernameToRemoteDB(username)
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { updateResponse ->

                    emit(Resource.Loading())

                    when (updateResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(updateResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            updateResponse.data?.let { successMsg ->
                                sessionCacheDataSource.cacheUsername(username).also {
                                    emit(Resource.Success(successMsg))
                                }
                            }
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
        }


    override suspend fun updateUserNotificationState(isNotificationEnabled: Boolean): Flow<Resource<String>> =
        flow<Resource<String>> {
            sessionRemoteDataSource.updateUserNotificationState(isNotificationEnabled)
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { updateResponse ->

                    emit(Resource.Loading())

                    when (updateResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(updateResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            updateResponse.data?.let { successMsg ->
                                sessionCacheDataSource.cacheUserNotificationState(
                                    isNotificationEnabled
                                )
                                    .also {
                                        emit(Resource.Success(successMsg))
                                    }

                            }
                        }
                        else -> {
                            /** NO-OP **/
                        }

                    }
                }
        }

    override suspend fun updateUserWeightToRemoteDB(weight: Double): Flow<Resource<String>> =
        flow<Resource<String>> {
            sessionRemoteDataSource.updateUserWeightToRemoteDB(weight)
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { updateResponse ->

                    emit(Resource.Loading())

                    when (updateResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(updateResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            updateResponse.data?.let { successMsg ->
                                sessionCacheDataSource.cacheUserWeight(weight).also {
                                    emit(Resource.Success(successMsg))
                                }
                            }
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
        }

    /**
     * Functions for fetching and saving from/to Jetpack DataStore
     */
    override suspend fun getFirstUseState(): Flow<Boolean> =
        sessionCacheDataSource.getFirstUseState()
            .flowOn(coroutineDispatchers.io())

    override suspend fun cacheFirstUseState(isFirstTime: Boolean) =
        sessionCacheDataSource.storeFirstUseState(isFirstTime)

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

    override suspend fun resetCachedUser() =
        sessionCacheDataSource.resetCachedUser()

    override suspend fun resetCachedStates() =
        sessionCacheDataSource.resetCachedStates()

    override suspend fun getUserUid(): Flow<String> =
        sessionCacheDataSource.getUserUid()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUsername(): Flow<String> =
        sessionCacheDataSource.getUsername()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserEmail(): Flow<String> =
        sessionCacheDataSource.getUserEmail()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserWeight(): Flow<Double> =
        sessionCacheDataSource.getUserWeight()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserNotificationState(): Flow<Boolean> =
        sessionCacheDataSource.getUserNotificationState()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrl(): Flow<String> =
        sessionCacheDataSource.getUserPhotoUrl()
            .flowOn(coroutineDispatchers.io())

    override suspend fun cacheFineLocationPermissionState(hasPermission: Boolean) =
        sessionCacheDataSource.cacheFineLocationPermissionState(hasPermission)

    override suspend fun cacheActivityRecognitionPermissionState(hasPermission: Boolean) =
        sessionCacheDataSource.cacheActivityRecognitionPermissionState(hasPermission)

    override suspend fun getFineLocationPermissionState(): Flow<Boolean> =
        sessionCacheDataSource.getFineLocationPermissionState()
            .flowOn(coroutineDispatchers.io())

    override suspend fun getActivityRecognitionPermissionState(): Flow<Boolean> =
        sessionCacheDataSource.getActivityRecognitionPermissionState()
            .flowOn(coroutineDispatchers.io())
}