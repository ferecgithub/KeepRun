package com.ferechamitbeyli.keeprun.framework.model.repositories

import com.ferechamitbeyli.keeprun.framework.model.remote.firebase.BaseAuthenticator
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authenticator : BaseAuthenticator
) : BaseAuthRepository {

    override suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser?
        = authenticator.signInWithEmailPassword(email , password)

    override suspend fun signUpWithEmailPassword(email: String, password: String): FirebaseUser?
        = authenticator.signUpWithEmailPassword(email , password)

    override fun signOut(): FirebaseUser? = authenticator.signOut()

    override fun getCurrentUser(): FirebaseUser? = authenticator.getUser()

    override suspend fun sendResetPassword(email: String): Boolean {
        authenticator.sendPasswordReset(email)
        return true
    }
}