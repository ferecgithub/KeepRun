package com.ferechamitbeyli.data.repositories.datasources.common

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.data.utils.DataConstants
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
import javax.inject.Inject

class SessionRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference,
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

        firebaseAuth.signOut().also {
            emit(Resource.Success("Successfully signed out."))
        }

    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun getCurrentUserFromRemoteDB(): Flow<Resource<User>> =
        flow<Resource<User>> {

            val currentUserUid = firebaseAuth.currentUser?.uid.toString()

            val dataSnapshotOfUserEntry =
                databaseReference.child(DataConstants.USERS_TABLE_REF).child(currentUserUid)

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

        val dataSnapshotOfUserUid =
            databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .child(DataConstants.USERS_TABLE_UID_REF)

        dataSnapshotOfUserUid.get().await().value.toString().also {
            emit(Resource.Success(it))
        }

    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserEmailFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            val dataSnapshotOfUserEmail =
                databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(DataConstants.USERS_TABLE_EMAIL_REF)

            dataSnapshotOfUserEmail.get().await().value.toString().also {
                emit(Resource.Success(it))
            }


        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserWeightFromRemoteDB(): Flow<Resource<Double>> =
        flow<Resource<Double>> {

            val dataSnapshotOfUserWeight =
                databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(DataConstants.USERS_TABLE_WEIGHT_REF)

            dataSnapshotOfUserWeight.get().await().value.toString().toDouble().also {
                emit(Resource.Success(it))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUsernameFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            val dataSnapshotOfUsername =
                databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(DataConstants.USERS_TABLE_USERNAME_REF)

            dataSnapshotOfUsername.get().await().value.toString().also { username ->
                if (username.isNotBlank()) {
                    emit(Resource.Success(username))
                } else {
                    emit(Resource.Error("The value does not exists in database."))
                }
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserNotificationStateFromRemoteDB(): Flow<Resource<Boolean>> =
        flow<Resource<Boolean>> {

            val dataSnapshotOfUserNotificationState =
                databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(DataConstants.USERS_TABLE_NOTIFICATION_ENABLE_REF)

            dataSnapshotOfUserNotificationState.get().await().value.toString().toBoolean().also {
                emit(Resource.Success(it))
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun getUserPhotoUrlFromRemoteDB(): Flow<Resource<String>> =
        flow<Resource<String>> {

            val dataSnapshotOfUserPhotoUrl =
                databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                    .child(DataConstants.USERS_TABLE_PHOTO_URL_REF)

            dataSnapshotOfUserPhotoUrl.get().await().value.toString().also {
                emit(Resource.Success(it))
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
                DataConstants.USERS_TABLE_UID_REF to firebaseAuth.currentUser?.uid.toString(),
                DataConstants.USERS_TABLE_EMAIL_REF to currentUser.email,
                DataConstants.USERS_TABLE_USERNAME_REF to currentUser.username,
                DataConstants.USERS_TABLE_NOTIFICATION_ENABLE_REF to currentUser.isNotificationEnable,
                DataConstants.USERS_TABLE_PHOTO_URL_REF to currentUser.photoUrl
            )

            databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .updateChildren(mappedUser).await()

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(currentUser.username)
                .build()

            firebaseAuth.currentUser?.apply {
                updateProfile(profileChangeRequest).await().also {
                    emit(Resource.Success("User is successfully updated."))
                }
            }

            /*
            if (onSuccessFlag == true) {
                emit(Resource.Success("User is successfully updated."))
            } else {
                emit(Resource.Error("An error occurred while updating the user."))
            }

             */

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUsernameToRemoteDB(username: String): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val mappedUsername = mapOf(
                DataConstants.USERS_TABLE_USERNAME_REF to username
            )

            databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .updateChildren(mappedUsername).await()

            val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()

            firebaseAuth.currentUser?.apply {
                updateProfile(profileChangeRequest).await().also {
                    emit(Resource.Success("Username is successfully updated."))
                }
            }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUserNotificationState(isNotificationEnabled: Boolean): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val mappedUserNotificationState = mapOf(
                DataConstants.USERS_TABLE_NOTIFICATION_ENABLE_REF to isNotificationEnabled
            )

            databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .updateChildren(mappedUserNotificationState).await().also {
                    emit(Resource.Success("Notification request is successfully updated."))
                }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUserWeightToRemoteDB(weight: Double): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            val mappedUserWeight = mapOf(
                DataConstants.USERS_TABLE_WEIGHT_REF to weight
            )

            databaseReference.child(DataConstants.USERS_TABLE_REF).child(firebaseAuth.uid!!)
                .updateChildren(mappedUserWeight).await().also {
                    emit(Resource.Success("Notification request is successfully updated."))
                }

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())

    override suspend fun updateUserPassword(password: String): Flow<Resource<String>> =
        flow<Resource<String>> {

            emit(Resource.Loading())

            firebaseAuth.currentUser?.updatePassword(password)?.await().also {
                emit(Resource.Success("Password is successfully changed."))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(coroutineDispatchers.io())
}


