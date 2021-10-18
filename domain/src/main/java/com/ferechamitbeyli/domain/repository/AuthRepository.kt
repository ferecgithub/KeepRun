package com.ferechamitbeyli.domain.repository

import com.ferechamitbeyli.domain.entity.User

class AuthRepository(
    private val dataSource: AuthDataSource
) {
    suspend fun signUpWithEmailPassword(email: String , password: String, username: String) = dataSource.signUp(email, password, username)
    suspend fun signInWithEmailPassword(email: String, password: String) = dataSource.signInWithEmailPassword(email, password)
    suspend fun sendResetPassword(email : String) = dataSource.sendResetPassword(email)
    suspend fun getCurrentUser() = dataSource.getCurrentUser()
    suspend fun signOut() = dataSource.signOut()
    suspend fun createUserInRemoteDB(user: User, any: Any) = dataSource.createUserInRemoteDB(user, any)
}