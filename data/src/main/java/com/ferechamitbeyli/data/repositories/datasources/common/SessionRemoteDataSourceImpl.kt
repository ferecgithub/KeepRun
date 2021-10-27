package com.ferechamitbeyli.data.repositories.datasources.common

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.data.utils.DomainMapper
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.datasources.common.SessionRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SessionRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private var userDtoMapper: DomainMapper<UserDto, User>,
    private val coroutineDispatchers: CoroutineDispatchers
) : SessionRemoteDataSource {

    override suspend fun getCurrentUser(): Flow<Resource<User>> = flow<Resource<User>> {
        val currentFirebaseUser = firebaseAuth.currentUser
        val userDto = UserDto(
            currentFirebaseUser?.uid,
            currentFirebaseUser?.email,
            currentFirebaseUser?.displayName,
            currentFirebaseUser?.photoUrl.toString()
        )
        emit(Resource.Success(userDtoMapper.mapToDomainModel(userDto)))
    }.flowOn(coroutineDispatchers.io())
}