package com.ferechamitbeyli.data.repositories

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.AuthRepository
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthDataSource
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthRemoteDBDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val authRemoteDBDataSource: AuthRemoteDBDataSource,
    private val coroutineDispatchers: CoroutineDispatchers
) : AuthRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<String>> = authDataSource.signUp(email, password, username).flowOn(coroutineDispatchers.io())

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<String>> = authDataSource.signInWithEmailPassword(email, password).flowOn(coroutineDispatchers.io())

    override suspend fun sendResetPassword(email: String): Flow<Resource<String>> =
        authDataSource.sendResetPassword(email).flowOn(coroutineDispatchers.io())

    override suspend fun signOut(): Flow<Resource<String>> = authDataSource.signOut().flowOn(coroutineDispatchers.io())

    override suspend fun createUserInRemoteDB(user: User): Flow<Resource<String>> = authRemoteDBDataSource.createUserInRemoteDB(user).flowOn(coroutineDispatchers.io())

}