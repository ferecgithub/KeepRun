package com.ferechamitbeyli.domain.repository.datasources.auth

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDBDataSource {
    suspend fun createUserInRemoteDB(user: User) : Flow<Resource<String>>
}