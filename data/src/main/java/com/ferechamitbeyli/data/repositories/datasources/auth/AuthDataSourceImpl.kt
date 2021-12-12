package com.ferechamitbeyli.data.repositories.datasources.auth

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val userDtoMapper: DomainMapper<UserDto, User>,
    private val firebaseAuth: FirebaseAuth,
    private val coroutineDispatchers: CoroutineDispatchers
) : AuthDataSource {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<User>> = flow<Resource<User>> {

        val data = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        data.user?.let {

            val userModel = UserDto(
                email = email,
                username = username,
                photoUrl = ""
            )

            // Subscribed to APP for Push Notifications upon registration.
            FirebaseMessaging.getInstance().subscribeToTopic("APP").await()

            emit(Resource.Success(userDtoMapper.mapToDomainModel(userModel)))

        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<User>> = flow<Resource<User>> {

        val data = firebaseAuth.signInWithEmailAndPassword(email, password).await()

        data?.let {

            if (firebaseAuth.currentUser?.isEmailVerified!!) {

                val userModel = UserDto(
                    email = data.user?.email.toString(),
                    username = data.user?.displayName.toString(),
                    photoUrl = data.user?.photoUrl.toString()
                )

                emit(Resource.Success(userDtoMapper.mapToDomainModel(userModel)))
            } else {
                firebaseAuth.currentUser?.sendEmailVerification()?.await().also {
                    emit(Resource.Error("Please verify email first."))
                }
            }
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun sendResetPassword(email: String): Flow<Resource<String>> =
        flow<Resource<String>> {

            firebaseAuth.sendPasswordResetEmail(email).await().also {
                emit(Resource.Success("Password reset email is sent."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)


}