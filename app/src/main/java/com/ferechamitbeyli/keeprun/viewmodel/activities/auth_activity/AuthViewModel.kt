package com.ferechamitbeyli.keeprun.viewmodel.activities.auth_activity

import android.util.Log
import androidx.lifecycle.*
import com.ferechamitbeyli.keeprun.framework.model.local.cache.DataStoreObject
import com.ferechamitbeyli.keeprun.framework.model.remote.enitities.enums.ValidationResults
import com.ferechamitbeyli.keeprun.framework.model.repositories.BaseAuthRepository
import com.ferechamitbeyli.keeprun.framework.common.validateEmail
import com.ferechamitbeyli.keeprun.framework.common.validatePassword
import com.ferechamitbeyli.keeprun.framework.common.validatePasswords
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: BaseAuthRepository,
    private val dataStoreObject: DataStoreObject
) : ViewModel() {

    private val TAG = "AuthViewModel"

    /**
     * Data and functions related to if onboarding/walkthrough screen finished or not
     */

    private var _isOnboardingFinished = MutableLiveData<Boolean>()
    val isOnboardingFinished: LiveData<Boolean> = _isOnboardingFinished


    fun assignOnboardingFinished(isFinished: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreObject.storeIfOnboardingFinished(isFinished)
        _isOnboardingFinished.postValue(isFinished)
    }

    fun getIfOnboardingFinished() = viewModelScope.launch(Dispatchers.IO) {
        dataStoreObject.getIsOnboardingFinished().collect {
            _isOnboardingFinished.postValue(it)
        }
    }

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = _firebaseUser

    private val _authEventsChannel = Channel<AllEvents>()
    val authEventsChannel = _authEventsChannel.receiveAsFlow()

    fun validateBeforeSignIn(email: String, password: String) = viewModelScope.launch {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        when {
            (emailValidation != ValidationResults.SUCCESS) -> {
                _authEventsChannel.send(AllEvents.ErrorCode(emailValidation))
            }
            (passwordValidation != ValidationResults.SUCCESS) -> {
                _authEventsChannel.send(AllEvents.ErrorCode(passwordValidation))
            }
            else -> {
                signInUser(email, password)
            }
        }
    }

    fun validateBeforeSignUp(email: String, password: String, confirmPass: String) =
        viewModelScope.launch {
            val emailValidation = validateEmail(email)
            val passwordsValidation = validatePasswords(password, confirmPass)

            when {
                (emailValidation != ValidationResults.SUCCESS) -> {
                    _authEventsChannel.send(AllEvents.ErrorCode(emailValidation))
                }
                (passwordsValidation != ValidationResults.SUCCESS) -> {
                    _authEventsChannel.send(AllEvents.ErrorCode(passwordsValidation))
                }
                else -> {
                    signUpUser(email, password)
                }
            }
        }

    private fun signInUser(email: String, password: String) = viewModelScope.launch {
        try {
            val user = authRepository.signInWithEmailPassword(email, password)
            user?.let {
                _firebaseUser.postValue(it)
                _authEventsChannel.send(AllEvents.Message("Login Success"))
            }
        } catch (e: Exception) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signInUser: ${error[1]}")
            _authEventsChannel.send(AllEvents.Error(error[1]))
        }
    }

    private fun signUpUser(email: String, password: String) = viewModelScope.launch {
        try {
            val user = authRepository.signUpWithEmailPassword(email, password)
            user?.let {
                _firebaseUser.postValue(it)
                _authEventsChannel.send(AllEvents.Message("Sign Up Success"))
            }
        } catch (e: Exception) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signInUser: ${error[1]}")
            _authEventsChannel.send(AllEvents.Error(error[1]))
        }
    }

    fun signOut() = viewModelScope.launch {
        try {
            val user = authRepository.signOut()
            user?.let {
                _authEventsChannel.send(AllEvents.Message("Logout Failure"))
            } ?: _authEventsChannel.send(AllEvents.Message("Sign Out Successful"))

            getCurrentUser()

        } catch (e: Exception) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signInUser: ${error[1]}")
            _authEventsChannel.send(AllEvents.Error(error[1]))
        }
    }

    fun getCurrentUser() = viewModelScope.launch {
        val user = authRepository.getCurrentUser()
        _firebaseUser.postValue(user)
    }

    fun verifySendPasswordReset(email: String) {
        val emailValidation = validateEmail(email)
        if (emailValidation != ValidationResults.SUCCESS) {
            viewModelScope.launch {
                _authEventsChannel.send(AllEvents.ErrorCode(emailValidation))
            }
        } else {
            sendPasswordResetEmail(email)
        }
    }

    private fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        try {
            val result = authRepository.sendResetPassword(email)
            if (result) {
                _authEventsChannel.send(AllEvents.Message("reset email sent"))
            } else {
                _authEventsChannel.send(AllEvents.Error("could not send password reset"))
            }
        } catch (e: Exception) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signInUser: ${error[1]}")
            _authEventsChannel.send(AllEvents.Error(error[1]))
        }
    }

    sealed class AllEvents {
        data class Message(val message: String) : AllEvents()
        data class ErrorCode(val result: ValidationResults) : AllEvents()
        data class Error(val error: String) : AllEvents()
    }


}