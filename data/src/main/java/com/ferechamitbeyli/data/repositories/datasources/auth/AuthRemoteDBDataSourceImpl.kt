package com.ferechamitbeyli.data.repositories.datasources.auth

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthRemoteDBDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDBDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val coroutineDispatchers: CoroutineDispatchers
) : AuthRemoteDBDataSource {
    override suspend fun createUserInRemoteDB(user: User): Flow<Resource<String>> = flow<Resource<String>>  {
        //val firebaseAuth = any as FirebaseAuth
        val firebaseUsersDbRef = Firebase.database.getReference("Users")
        firebaseUsersDbRef.child(firebaseAuth.uid!!).setValue(user).await()
        val profileChangeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(user.username)
            .build()
        firebaseAuth.currentUser?.apply {
            updateProfile(profileChangeRequest).await()
            sendEmailVerification().await()
        }
        emit(Resource.Success("User is successfully created."))
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(coroutineDispatchers.io())
}