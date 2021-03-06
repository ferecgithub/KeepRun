package com.ferechamitbeyli.domain.repository.datasources.auth

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    suspend fun signUp(email: String, password: String, username: String): Flow<Resource<User>>
    suspend fun signInWithEmailPassword(email: String, password: String): Flow<Resource<User>>
    suspend fun sendResetPassword(email : String): Flow<Resource<String>>
}