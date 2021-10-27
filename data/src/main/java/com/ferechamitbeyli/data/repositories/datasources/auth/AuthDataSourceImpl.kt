package com.ferechamitbeyli.data.repositories.datasources.auth

import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.data.utils.DomainMapper
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthDataSource
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthRemoteDBDataSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private var authRemoteDBDataSource: AuthRemoteDBDataSource,
    private var userDtoMapper: DomainMapper<UserDto, User>,
    private var firebaseAuth: FirebaseAuth,
    private val coroutineDispatchers: CoroutineDispatchers
) : AuthDataSource {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<String>> = flow<Resource<String>> {
        emit(Resource.Loading())
        //val auth = Firebase.auth
        val data = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        data.user?.let {
            val userModel = UserDto(
                it.uid, email, username, ""
            )
            authRemoteDBDataSource.createUserInRemoteDB(userDtoMapper.mapToDomainModel(userModel))
            emit(Resource.Success("Email verification is sent."))
        }
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<String>> = flow<Resource<String>> {
        emit(Resource.Loading())

        //val auth = Firebase.auth
        val data = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        data?.let {
            if (firebaseAuth.currentUser?.isEmailVerified!!) {
                emit(Resource.Success("Login is successful."))
            } else {
                firebaseAuth.currentUser?.sendEmailVerification()?.await()
                emit(Resource.Error("Please verify email first."))
            }
        }
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(coroutineDispatchers.io())

    override suspend fun sendResetPassword(email: String): Flow<Resource<String>> =
        flow<Resource<String>> {
            emit(Resource.Loading())
            //val auth = Firebase.auth
            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(Resource.Success("Password reset email sent."))
        }.catch {
            emit(Resource.Error(it.message!!))
        }.flowOn(Dispatchers.IO)

    override suspend fun signOut(): Flow<Resource<String>> = flow<Resource<String>> {
        firebaseAuth.signOut()
        emit(Resource.Success("Successfully signed out."))
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(coroutineDispatchers.io())

    /*
    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<String>> = flow<Resource<String>> {
        emit(Resource.Loading())

        val auth = Firebase.auth
        val data = auth.createUserWithEmailAndPassword(email, password).await()
        data.user?.let {
            val userEntity = UserDto(
                it.uid, email, username, ""
            )

            createUserInRemoteDB(userEntity.toUser(), auth)

            emit(Resource.Success("Email verification is sent."))
        }
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(Dispatchers.IO)

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<String>> = flow<Resource<String>> {
        emit(Resource.Loading())

        val auth = Firebase.auth
        val data = auth.signInWithEmailAndPassword(email, password).await()
        data?.let {
            if (auth.currentUser?.isEmailVerified!!) {
                emit(Resource.Success("Login is successful."))
            } else {
                auth.currentUser?.sendEmailVerification()?.await()
                emit(Resource.Error("Please verify email first."))
            }
        }
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(Dispatchers.IO)

    override suspend fun sendResetPassword(email: String): Flow<Resource<String>> =
        flow<Resource<String>> {
            emit(Resource.Loading())
            val auth = Firebase.auth
            auth.sendPasswordResetEmail(email).await()
            emit(Resource.Success("Password reset email sent."))
        }.catch {
            emit(Resource.Error(it.message!!))
        }.flowOn(Dispatchers.IO)

    override suspend fun getCurrentUser(): Flow<Resource<String>> = flow<Resource<String>> {
        emit(Resource.Success(""))
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(Dispatchers.IO)

    override suspend fun signOut(): Flow<Resource<String>> = flow<Resource<String>> {
        Firebase.auth.signOut()
        emit(Resource.Success("Successfully signed out."))
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(Dispatchers.IO)

    override suspend fun createUserInRemoteDB(user: User, any: Any) = flow<Resource<String>>  {
        val firebaseAuth = any as FirebaseAuth
        val firebaseUsersDbRef = Firebase.database.getReference("Users")
        firebaseUsersDbRef.child(any.uid!!).setValue(user).await()
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
    }.flowOn(Dispatchers.IO)


}

     */

/*
class FirebaseAuthenticator : BaseAuthenticator {

    override suspend fun signUpWithEmailPassword(email: String, password: String): FirebaseUser? {
        Firebase.auth.createUserWithEmailAndPassword(email,password).await()
        return Firebase.auth.currentUser
    }

    override suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser? {
        Firebase.auth.signInWithEmailAndPassword(email , password).await()
        return Firebase.auth.currentUser
    }

    override fun signOut(): FirebaseUser? {
        Firebase.auth.signOut()
        return Firebase.auth.currentUser
    }

    override fun getUser(): FirebaseUser? = Firebase.auth.currentUser

    override suspend fun sendPasswordReset(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
    }
}

 */
}