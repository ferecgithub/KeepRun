package com.ferechamitbeyli.keeprun.framework.model.remote.firebase

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.keeprun.framework.model.remote.enitities.UserEntity
import kotlinx.coroutines.flow.Flow


interface BaseAuthenticator {

    suspend fun signUp(email: String, password: String, username: String): Flow<Resource<Any>>
    suspend fun signInWithEmailPassword(email: String, password: String): Flow<Resource<Any>>
    suspend fun sendResetPassword(email : String): Flow<Resource<Any>>
    suspend fun getCurrentUser(): Flow<Resource<Any>>
    suspend fun signOut(): Flow<Resource<Any>>
    suspend fun createUserInRemoteDB(userEntity: UserEntity, any: Any)

}
