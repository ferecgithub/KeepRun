package com.ferechamitbeyli.data.repositories.datasources.auth

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthDataSource
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthRemoteDBDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionCacheDataSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val sessionCacheDataSource: SessionCacheDataSource,
    private val authRemoteDBDataSource: AuthRemoteDBDataSource,
    private val userDtoMapper: DomainMapper<UserDto, User>,
    private val firebaseAuth: FirebaseAuth,
    private val coroutineDispatchers: CoroutineDispatchers
) : AuthDataSource {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<String>> = flow<Resource<String>> {
        emit(Resource.Loading())

        val data = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        data.user?.let { user ->

            val userModel = UserDto(
                user.uid, email, username, true, ""
            )
            authRemoteDBDataSource.createUserInRemoteDB(userDtoMapper.mapToDomainModel(userModel))
                .catch { dbError ->
                    emit(Resource.Error(dbError.message.toString()))
                }.collect{
                    if (it is Resource.Success) {
                        emit(Resource.Success(it.data.toString()))
                    } else {
                        emit(Resource.Error(it.message.toString()))
                    }
                }

        }
        //emit(Resource.Success("Sign up is successful."))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<String>> = flow<Resource<String>> {
        emit(Resource.Loading())

        val data = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        data?.let {
            if (firebaseAuth.currentUser?.isEmailVerified!!) {
                sessionCacheDataSource.cacheUserAccount(
                    data.user?.uid.toString(),
                    data.user?.displayName.toString(),
                    data.user?.email.toString(),
                    true,
                    data.user?.photoUrl.toString()
                )
                emit(Resource.Success("Login is successful."))
            } else {
                firebaseAuth.currentUser?.sendEmailVerification()?.await()
                emit(Resource.Error("Please verify email first."))
            }
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun sendResetPassword(email: String): Flow<Resource<String>> =
        flow<Resource<String>> {
            emit(Resource.Loading())

            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(Resource.Success("Password reset email is sent."))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)


}