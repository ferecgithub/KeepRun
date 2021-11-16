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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import logcat.logcat
import javax.inject.Inject

class SessionRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference,
    private var userDtoMapper: DomainMapper<UserDto, User>,
    private val coroutineDispatchers: CoroutineDispatchers
) : SessionRemoteDataSource {

    override suspend fun getCurrentUserIdentifier(): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            emit(Resource.Success(firebaseAuth.currentUser?.uid.toString()))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun signOut(): Flow<Resource<String>> = flow<Resource<String>> {
        firebaseAuth.signOut()
        emit(Resource.Loading())
        emit(Resource.Success("Successfully signed out."))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun getCurrentUserFromRemoteDB(identifier: String): Flow<Resource<User>> =
        flow<Resource<User>> {

            emit(Resource.Loading())

            val dataSnapshotOfUserEntry =
                databaseReference.child(Constants.USERS_TABLE_REF).child(identifier)
            var currentUser: UserDto? = null

            dataSnapshotOfUserEntry.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        currentUser = snapshot.getValue(UserDto::class.java)!!
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    /** NO-OP **/
                }

            })

            if (currentUser != null) {
                emit(Resource.Success(userDtoMapper.mapToDomainModel(currentUser!!)))
            } else {
                emit(Resource.Error("The value does not exists in database."))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())


    override suspend fun getUserUidFromRemoteDB(): Flow<Resource<String>> = flow<Resource<String>> {

        emit(Resource.Loading())

        val dataSnapshotOfUserUid =
            databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .child(Constants.USERS_TABLE_UID_REF)
        val userUid: String = dataSnapshotOfUserUid.get().await().value.toString()

        emit(Resource.Success(userUid))

    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserEmailFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            /*
            val firebaseRealtimeDbRef =
                FirebaseDatabase.getInstance(FIREBASE_DB_REF).reference

             */
            val dataSnapshotOfUserEmail =
                databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(Constants.USERS_TABLE_EMAIL_REF)
            val userEmail: String = dataSnapshotOfUserEmail.get().await().value.toString()

            emit(Resource.Success(userEmail))

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserWeightFromRemoteDB(): Flow<Resource<Double>> =
        flow<Resource<Double>> {

            emit(Resource.Loading())

            val dataSnapshotOfUserWeight =
                databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(Constants.USERS_TABLE_WEIGHT_REF)
            val weight: Double = dataSnapshotOfUserWeight.get().await().value.toString().toDouble()

            emit(Resource.Success(weight))

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUsernameFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val dataSnapshotOfUsername =
                databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(Constants.USERS_TABLE_USERNAME_REF)
            val username: String = dataSnapshotOfUsername.get().await().value.toString()

            if (username.isNotBlank()) {
                emit(Resource.Success(username))
                logcat("USERNAME_SUCC") { username }
            } else {
                emit(Resource.Error("The value does not exists in database."))
                logcat("USERNAME_ERR") { "" }
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserNotificationStateFromRemoteDB(): Flow<Resource<Boolean>> =
        flow<Resource<Boolean>> {

            emit(Resource.Loading())

            val dataSnapshotOfUserNotificationState =
                databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(Constants.USERS_TABLE_NOTIFICATION_ENABLE_REF)
            val notificationState: Boolean =
                dataSnapshotOfUserNotificationState.get().await().value.toString().toBoolean()

            emit(Resource.Success(notificationState))

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrlFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val dataSnapshotOfUserPhotoUrl =
                databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(Constants.USERS_TABLE_PHOTO_URL_REF)
            val userPhotoUrl: String = dataSnapshotOfUserPhotoUrl.get().await().value.toString()

            emit(Resource.Success(userPhotoUrl))

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUserChangesToRemoteDB(user: User): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            var onSuccessFlag: Boolean? = null

            val currentUser = userDtoMapper.mapFromDomainModel(user)

            val mappedUser = mapOf(
                Constants.USERS_TABLE_UID_REF to firebaseAuth.currentUser?.uid.toString(),
                Constants.USERS_TABLE_EMAIL_REF to currentUser.email,
                Constants.USERS_TABLE_USERNAME_REF to currentUser.username,
                Constants.USERS_TABLE_NOTIFICATION_ENABLE_REF to currentUser.isNotificationEnable,
                Constants.USERS_TABLE_PHOTO_URL_REF to currentUser.photoUrl
            )

            databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
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
                Constants.USERS_TABLE_USERNAME_REF to username
            )

            databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .child(Constants.USERS_TABLE_USERNAME_REF).updateChildren(mappedUsername).await()

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
                Constants.USERS_TABLE_NOTIFICATION_ENABLE_REF to isNotificationEnabled
            )

            databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .child(Constants.USERS_TABLE_NOTIFICATION_ENABLE_REF)
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

    override suspend fun updateUserWeightToRemoteDB(weight: Double): Flow<Resource<String>> =
        flow<Resource<String>> {
            emit(Resource.Loading())

            var onSuccessFlag: Boolean? = null

            val mappedUserWeight = mapOf(
                Constants.USERS_TABLE_WEIGHT_REF to weight
            )

            databaseReference.child(Constants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                //.child(Constants.USER_TABLE_WEIGHT_REF)
                .updateChildren(mappedUserWeight).addOnCompleteListener {
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


