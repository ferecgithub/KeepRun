package com.ferechamitbeyli.data.repositories.datasources.common

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.data.utils.Constants
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.datasources.common.SessionRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SessionRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private var userDtoMapper: DomainMapper<UserDto, User>,
    private val coroutineDispatchers: CoroutineDispatchers
) : SessionRemoteDataSource {

    override suspend fun getCurrentUserIdentifier(): Flow<Resource<String>> =
        flow<Resource<String>> {
            emit(Resource.Success(firebaseAuth.currentUser?.uid.toString()))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun signOut(): Flow<Resource<String>> = flow<Resource<String>> {
        firebaseAuth.signOut()
        emit(Resource.Success("Successfully signed out."))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun getCurrentUserFromRemoteDB(identifier: String): Flow<Resource<User>> =
        flow<Resource<User>> {

            emit(Resource.Loading())

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val dataSnapshotOfUserEntry =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(identifier)
                    .get().await()

            if (dataSnapshotOfUserEntry.exists()) {
                val userUid: String =
                    dataSnapshotOfUserEntry.child(Constants.USER_TABLE_UID_REF).value as String
                val username: String =
                    dataSnapshotOfUserEntry.child(Constants.USER_TABLE_USERNAME_REF).value as String
                val userEmail: String =
                    dataSnapshotOfUserEntry.child(Constants.USER_TABLE_EMAIL_REF).value as String
                val userNotificationEnabled: Boolean =
                    dataSnapshotOfUserEntry.child(Constants.USER_TABLE_NOTIFICATION_ENABLE_REF).value as Boolean
                val userPhotoUrl: String =
                    dataSnapshotOfUserEntry.child(Constants.USER_TABLE_PHOTO_URL_REF).value as String

                val userDto = UserDto(
                    userUid,
                    userEmail,
                    username,
                    userNotificationEnabled,
                    userPhotoUrl
                )

                emit(Resource.Success(userDtoMapper.mapToDomainModel(userDto)))
            } else {
                emit(Resource.Error("The value does not exists in database."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserUidFromRemoteDB(): Flow<Resource<String>> = flow<Resource<String>> {

        emit(Resource.Loading())

        val firebaseRealtimeDbRef =
            FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
        val dataSnapshotOfUserEntry =
            firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .get().await()

        if (dataSnapshotOfUserEntry.exists()) {
            val userUid =
                dataSnapshotOfUserEntry.child(Constants.USER_TABLE_UID_REF).value as String
            emit(Resource.Success(userUid))
        } else {
            emit(Resource.Error("The value does not exists in database."))
        }

    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun getUsernameFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val dataSnapshotOfUserEntry =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .get().await()

            if (dataSnapshotOfUserEntry.exists()) {
                val username =
                    dataSnapshotOfUserEntry.child(Constants.USER_TABLE_USERNAME_REF).value as String
                emit(Resource.Success(username))
            } else {
                emit(Resource.Error("The value does not exists in database."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserNotificationStateFromRemoteDB(): Flow<Resource<Boolean>> =
        flow<Resource<Boolean>> {

            emit(Resource.Loading())

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val dataSnapshotOfUserEntry =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .get().await()

            if (dataSnapshotOfUserEntry.exists()) {
                val isNotificationEnabled =
                    dataSnapshotOfUserEntry.child(Constants.USER_TABLE_NOTIFICATION_ENABLE_REF).value as Boolean
                emit(Resource.Success(isNotificationEnabled))
            } else {
                emit(Resource.Error("The value does not exists in database."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrlFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val dataSnapshotOfUserEntry =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .get().await()

            if (dataSnapshotOfUserEntry.exists()) {
                val photoUrl =
                    dataSnapshotOfUserEntry.child(Constants.USER_TABLE_PHOTO_URL_REF).value as String
                emit(Resource.Success(photoUrl))
            } else {
                emit(Resource.Error("The value does not exists in database."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUserChangesToRemoteDB(user: User): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            var onSuccessFlag: Boolean? = null

            val currentUser = userDtoMapper.mapFromDomainModel(user)

            val mappedUser = mapOf(
                "uid" to currentUser.uid,
                "email" to currentUser.email,
                "username" to currentUser.username,
                "isNotificationEnable" to currentUser.isNotificationEnable,
                "photoUrl" to currentUser.photoUrl
            )

            val firebaseUsersDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            firebaseUsersDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .updateChildren(mappedUser).await()

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(currentUser.username)
                .build()

            firebaseAuth.currentUser?.apply {
                updateProfile(profileChangeRequest).addOnCompleteListener {
                    onSuccessFlag = it.isSuccessful

                }
            }

            if (onSuccessFlag == true) {
                emit(Resource.Success("User is successfully updated."))
            } else {
                emit(Resource.Error("An error occurred while updating the user."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUsernameToRemoteDB(username: String): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            var onSuccessFlag: Boolean? = null

            val mappedUsername = mapOf(
                "username" to username
            )

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .child(Constants.USER_TABLE_USERNAME_REF).updateChildren(mappedUsername).await()

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()

            firebaseAuth.currentUser?.apply {
                updateProfile(profileChangeRequest).addOnCompleteListener {
                    onSuccessFlag = it.isSuccessful
                }
            }

            if (onSuccessFlag == true) {
                emit(Resource.Success("Username is successfully updated."))
            } else {
                emit(Resource.Error("An error occurred while updating the username."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUserNotificationState(isNotificationEnabled: Boolean): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            var onSuccessFlag: Boolean? = null

            val mappedUserNotificationState = mapOf(
                "isNotificationEnable" to isNotificationEnabled
            )

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .child(Constants.USER_TABLE_NOTIFICATION_ENABLE_REF)
                .updateChildren(mappedUserNotificationState).addOnCompleteListener {
                    onSuccessFlag = it.isSuccessful
                }

            if (onSuccessFlag == true) {
                emit(Resource.Success("Notification request is successfully updated."))
            } else {
                emit(Resource.Error("An error occurred while updating the notification request."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())


}