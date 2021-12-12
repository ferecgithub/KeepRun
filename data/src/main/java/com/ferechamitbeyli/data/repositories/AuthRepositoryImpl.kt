package com.ferechamitbeyli.data.repositories

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.repository.AuthRepository
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthDataSource
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthRemoteDBDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionCacheDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authDataSource: AuthDataSource,
    private val authRemoteDBDataSource: AuthRemoteDBDataSource,
    private val sessionRemoteDataSource: SessionRemoteDataSource,
    private val sessionCacheDataSource: SessionCacheDataSource,
    private val coroutineDispatchers: CoroutineDispatchers
) : AuthRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<String>> = flow<Resource<String>> {

        authDataSource.signUp(email, password, username)
            .catch { emit(Resource.Error(it.message.toString(), null)) }
            .flowOn(coroutineDispatchers.io())
            .collect { userResponse ->

                emit(Resource.Loading())

                when (userResponse) {
                    is Resource.Error -> {
                        emit(Resource.Error(userResponse.message.toString()))
                    }
                    is Resource.Success -> {

                        userResponse.data?.let { user ->
                            authRemoteDBDataSource.createUserInRemoteDB(user)
                                .collect { dbResponse ->
                                    when (dbResponse) {
                                        is Resource.Error -> {
                                            emit(Resource.Error(userResponse.message.toString()))
                                        }
                                        is Resource.Success -> {
                                            emit(Resource.Success("User is successfully created."))
                                        }
                                        else -> {
                                            /** NO-OP **/
                                        }
                                    }
                                }
                        }
                    }
                    else -> {
                        /** NO-OP **/
                    }
                }
            }
    }

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<String>> = flow<Resource<String>> {

        authDataSource.signInWithEmailPassword(email, password)
            .catch { emit(Resource.Error(it.message.toString(), null)) }
            .flowOn(coroutineDispatchers.io())
            .collect { userResponse ->

                emit(Resource.Loading())

                when (userResponse) {
                    is Resource.Error -> {
                        emit(Resource.Error(userResponse.message.toString()))
                    }
                    is Resource.Success -> {
                        combine(
                            sessionRemoteDataSource.getUserWeightFromRemoteDB(),
                            sessionRemoteDataSource.getUserNotificationStateFromRemoteDB()
                        ) { weight, notificationState ->

                            if (weight.data == null || notificationState.data == null) {
                                throw Exception("Can not fetch data. Please try again.")
                            }

                            UserDto(
                                email = userResponse.data?.email.toString(),
                                username = userResponse.data?.username.toString(),
                                photoUrl = userResponse.data?.photoUrl.toString(),
                                weight = weight.data.toString().toDouble(),
                                isNotificationEnable = notificationState.data.toString().toBoolean()
                            )

                        }.catch {
                            emit(Resource.Error(it.message.toString(), null))
                        }.collect { user ->

                            sessionCacheDataSource.cacheUserAccount(
                                userUid = firebaseAuth.currentUser?.uid.toString(),
                                username = user.username.toString(),
                                userEmail = user.email.toString(),
                                userWeight = user.weight,
                                userNotificationEnabled = user.isNotificationEnable,
                                userPhotoUrl = user.photoUrl.toString()

                            ).also {
                                emit(Resource.Success("Login is successful."))
                            }
                        }
                    }
                    else -> {
                        /** NO-OP **/
                    }
                }
            }
    }

    override suspend fun sendResetPassword(email: String): Flow<Resource<String>> =
        flow<Resource<String>> {
            authDataSource.sendResetPassword(email)
                .catch { emit(Resource.Error(it.message.toString(), null)) }
                .flowOn(coroutineDispatchers.io())
                .collect { resetPassResponse ->

                    emit(Resource.Loading())

                    when (resetPassResponse) {
                        is Resource.Error -> {
                            emit(Resource.Error(resetPassResponse.message.toString()))
                        }
                        is Resource.Success -> {
                            emit(Resource.Success(resetPassResponse.data.toString()))
                        }
                        else -> {
                            /** NO-OP **/
                        }
                    }
                }
        }

}