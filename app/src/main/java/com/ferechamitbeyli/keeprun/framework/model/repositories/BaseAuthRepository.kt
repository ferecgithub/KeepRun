package com.ferechamitbeyli.keeprun.framework.model.repositories

import com.google.firebase.auth.FirebaseUser


interface BaseAuthRepository {

    suspend fun signInWithEmailPassword(email:String , password:String): FirebaseUser?

    suspend fun signUpWithEmailPassword(email: String , password: String): FirebaseUser?

    fun signOut() : FirebaseUser?

    fun getCurrentUser() : FirebaseUser?

    suspend fun sendResetPassword(email : String) : Boolean

}