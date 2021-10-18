package com.ferechamitbeyli.keeprun.framework.model.repositories

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.keeprun.framework.model.remote.firebase.BaseAuthenticator
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authenticator : BaseAuthenticator
) : BaseAuthRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<Any>> {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<Any>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendResetPassword(email: String): Flow<Resource<Any>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): Flow<Resource<Any>> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(): Flow<Resource<Any>> {
        TODO("Not yet implemented")
    }


    /*
    override suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser
        = authenticator.signInWithEmailPassword(email , password)

    override suspend fun signUpWithEmailPassword(email: String, password: String): FirebaseUser?
        = authenticator.signUpWithEmailPassword(email , password)

    override fun signOut(): FirebaseUser? = authenticator.signOut()

    override fun getCurrentUser(): FirebaseUser? = authenticator.getUser()

    override suspend fun sendResetPassword(email: String): Boolean {
        authenticator.sendPasswordReset(email)
        return true
    }

     */
}