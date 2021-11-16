package com.ferechamitbeyli.data.repositories.datasources.auth

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.data.utils.Constants.USERS_TABLE_REF
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthRemoteDBDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDBDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference,
    private val userDtoMapper: DomainMapper<UserDto, User>,
    private val coroutineDispatchers: CoroutineDispatchers
) : AuthRemoteDBDataSource {

    override suspend fun createUserInRemoteDB(user: User): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            var onSuccessFlag: Boolean? = null

            val currentUser = userDtoMapper.mapFromDomainModel(user)

            databaseReference.child(USERS_TABLE_REF).child(firebaseAuth.uid.toString())
                .setValue(currentUser)

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(currentUser.username)
                .build()

            firebaseAuth.currentUser?.apply {

                updateProfile(profileChangeRequest).addOnCompleteListener {
                    onSuccessFlag = it.isSuccessful
                }
                sendEmailVerification().await()

                if (onSuccessFlag == true) {
                    emit(Resource.Success("User is successfully created."))
                } else {
                    emit(Resource.Error("An error occurred while creating the user."))
                }
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())
}