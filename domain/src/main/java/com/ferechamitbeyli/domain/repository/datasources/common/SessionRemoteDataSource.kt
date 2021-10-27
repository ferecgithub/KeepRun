package com.ferechamitbeyli.domain.repository.datasources.common

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface SessionRemoteDataSource {
    suspend fun getCurrentUser() : Flow<Resource<User>>
}