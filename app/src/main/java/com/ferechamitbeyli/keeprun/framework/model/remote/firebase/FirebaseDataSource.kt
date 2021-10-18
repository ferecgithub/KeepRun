package com.ferechamitbeyli.keeprun.framework.model.remote.firebase

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.AuthDataSource
import com.ferechamitbeyli.keeprun.framework.model.remote.enitities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class FirebaseDataSource : AuthDataSource {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<Any>> = flow<Resource<Any>> {
        emit(Resource.Loading())

        val auth = Firebase.auth
        val data = auth.createUserWithEmailAndPassword(email, password).await()
        data.user?.let {
            val userEntity = UserEntity(
                email, username
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
    ): Flow<Resource<Any>> = flow<Resource<Any>> {
        emit(Resource.Loading())

        val auth = Firebase.auth
        val data = auth.signInWithEmailAndPassword(email, password).await()
        data?.let {
            if (auth.currentUser?.isEmailVerified!!) {
                emit(Resource.Success("Login is successful"))
            } else {
                auth.currentUser?.sendEmailVerification()?.await()
                emit(Resource.Error("Verify email first"))
            }
        }
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(Dispatchers.IO)

    override suspend fun sendResetPassword(email: String): Flow<Resource<Any>> =
        flow<Resource<Any>> {
            emit(Resource.Loading())
            val auth = Firebase.auth
            auth.sendPasswordResetEmail(email).await()
            emit(Resource.Success("Password reset email sent."))
        }.catch {
            emit(Resource.Error(it.message!!))
        }.flowOn(Dispatchers.IO)

    override suspend fun getCurrentUser(): Flow<Resource<Any>> = flow<Resource<Any>> {
        emit(Resource.Success(""))
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(Dispatchers.IO)

    override suspend fun signOut(): Flow<Resource<Any>> = flow<Resource<Any>> {
        Firebase.auth.signOut()
        emit(Resource.Success("Successfully signed out"))
    }.catch {
        emit(Resource.Error(it.message!!))
    }.flowOn(Dispatchers.IO)

    override suspend fun createUserInRemoteDB(user: User, any: Any) {
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
    }


}

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