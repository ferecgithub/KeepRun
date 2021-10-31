package com.ferechamitbeyli.domain.repository.datasources.auth

import com.ferechamitbeyli.domain.Resource
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    suspend fun signUp(email: String, password: String, username: String): Flow<Resource<String>>
    suspend fun signInWithEmailPassword(email: String, password: String): Flow<Resource<String>>
    suspend fun sendResetPassword(email : String): Flow<Resource<String>>
}