package com.ferechamitbeyli.data.repositories.datasources.common

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.data.remote.entities.mappers.UserDtoMapper
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

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val dataSnapshotOfUserEntry =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
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

                emit(Resource.Success(UserDtoMapper.mapToDomainModel(userDto)))
            } else {
                emit(Resource.Error("The value does not exists in database."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserUidFromRemoteDB(): Flow<Resource<String>> = flow<Resource<String>> {

        val firebaseRealtimeDbRef =
            FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
        val dataSnapshotOfUserEntry =
            firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .get().await()

        if (dataSnapshotOfUserEntry.exists()) {
            val userUid = dataSnapshotOfUserEntry.child(Constants.USER_TABLE_UID_REF).value as String
            emit(Resource.Success(userUid))
        } else {
            emit(Resource.Error("The value does not exists in database."))
        }

    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun getUsernameFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val dataSnapshotOfUserEntry =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .get().await()

            if (dataSnapshotOfUserEntry.exists()) {
                val username = dataSnapshotOfUserEntry.child(Constants.USER_TABLE_USERNAME_REF).value as String
                emit(Resource.Success(username))
            } else {
                emit(Resource.Error("The value does not exists in database."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserNotificationStateFromRemoteDB(): Flow<Resource<Boolean>> =
        flow<Resource<Boolean>> {

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val dataSnapshotOfUserEntry =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .get().await()

            if (dataSnapshotOfUserEntry.exists()) {
                val isNotificationEnabled = dataSnapshotOfUserEntry.child(Constants.USER_TABLE_NOTIFICATION_ENABLE_REF).value as Boolean
                emit(Resource.Success(isNotificationEnabled))
            } else {
                emit(Resource.Error("The value does not exists in database."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrlFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val dataSnapshotOfUserEntry =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .get().await()

            if (dataSnapshotOfUserEntry.exists()) {
                val photoUrl = dataSnapshotOfUserEntry.child(Constants.USER_TABLE_PHOTO_URL_REF).value as String
                emit(Resource.Success(photoUrl))
            } else {
                emit(Resource.Error("The value does not exists in database."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUserChangesToRemoteDB(user: User): Flow<Resource<String>> =
        flow<Resource<String>> {

            val currentUser = UserDtoMapper.mapFromDomainModel(user)

            val firebaseUsersDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val isProcessSuccessful =
                firebaseUsersDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .setValue(currentUser).isSuccessful

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(currentUser.username)
                .build()

            firebaseAuth.currentUser?.apply {
                updateProfile(profileChangeRequest).await()
            }

            if (isProcessSuccessful) {
                emit(Resource.Success("User is successfully updated."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUsernameToRemoteDB(username: String): Flow<Resource<String>> =
        flow<Resource<String>> {

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val isProcessSuccessful =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(Constants.USER_TABLE_USERNAME_REF).setValue(username).isSuccessful

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()

            firebaseAuth.currentUser?.apply {
                updateProfile(profileChangeRequest).await()
            }

            if (isProcessSuccessful) {
                emit(Resource.Success("Username is successfully updated."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUserNotificationState(isNotificationEnabled: Boolean): Flow<Resource<String>> =
        flow<Resource<String>> {

            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DB_REF).reference
            val isProcessSuccessful =
                firebaseRealtimeDbRef.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(Constants.USER_TABLE_NOTIFICATION_ENABLE_REF)
                    .setValue(isNotificationEnabled).isSuccessful

            if (isProcessSuccessful) {
                emit(Resource.Success("Username is successfully updated."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())


}