package com.ferechamitbeyli.data.repositories

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.AuthRepository
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthDataSource
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthRemoteDBDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val authRemoteDBDataSource: AuthRemoteDBDataSource
) : AuthRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<String>> = authDataSource.signUp(email, password, username)

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<String>> = authDataSource.signInWithEmailPassword(email, password)

    override suspend fun sendResetPassword(email: String): Flow<Resource<String>> =
        authDataSource.sendResetPassword(email)

    override suspend fun createUserInRemoteDB(user: User): Flow<Resource<String>> =
        authRemoteDBDataSource.createUserInRemoteDB(user)

}