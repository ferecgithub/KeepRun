package com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.helpers.ValidationHelperFunctions.validateEmail
import com.ferechamitbeyli.presentation.utils.helpers.ValidationHelperFunctions.validatePassword
import com.ferechamitbeyli.presentation.utils.helpers.ValidationHelperFunctions.validatePasswords
import com.ferechamitbeyli.presentation.utils.helpers.ValidationHelperFunctions.validateUsername
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.states.ValidationState
import com.ferechamitbeyli.presentation.utils.usecases.AuthUseCases
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val sessionUseCases: SessionUseCases,
    coroutineDispatchers: CoroutineDispatchers,
    networkConnectionTracker: NetworkConnectionTracker
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    private var _authValidationEventsChannel = MutableSharedFlow<ValidationState>()
    val authValidationEventsChannel: MutableSharedFlow<ValidationState> =
        _authValidationEventsChannel

    private var _authEventsChannel = MutableSharedFlow<EventState>()
    val authEventsChannel: MutableSharedFlow<EventState> = _authEventsChannel

    fun validateBeforeSignIn(email: String, password: String) =
        defaultScope.launch {
            val emailValidation = validateEmail(email)
            val passwordValidation = validatePassword(password)

            _authValidationEventsChannel.emit(emailValidation)
            _authValidationEventsChannel.emit(passwordValidation)
        }

    fun validateBeforeSignUp(
        username: String,
        email: String,
        password: String,
        confirmPass: String
    ) =
        defaultScope.launch {
            val emailValidation = validateEmail(email)
            val passwordsValidation = validatePasswords(password, confirmPass)
            val usernameValidation = validateUsername(username)

            _authValidationEventsChannel.emit(emailValidation)
            _authValidationEventsChannel.emit(passwordsValidation)
            _authValidationEventsChannel.emit(usernameValidation)
        }

    fun validateBeforePasswordReset(email: String) =
        defaultScope.launch {
            val emailValidation = validateEmail(email)
            _authValidationEventsChannel.emit(emailValidation)
        }

    fun signInUser(email: String, password: String) =
        ioScope.launch {
            authUseCases.signInUseCase.invoke(email, password).collect {
                when (it) {
                    is Resource.Success -> {
                        _authEventsChannel.emit(EventState.Success(it.data))
                    }
                    is Resource.Error -> {
                        _authEventsChannel.emit(EventState.Error(it.message.toString()))
                    }
                    is Resource.Loading -> _authEventsChannel.emit(EventState.Loading())
                }
            }
        }

    fun signUpUser(email: String, password: String, username: String) =
        ioScope.launch {
            authUseCases.signUpUseCase.invoke(email, password, username).collect {
                when (it) {
                    is Resource.Success -> {
                        _authEventsChannel.emit(EventState.Success(it.data))
                    }
                    is Resource.Error -> {
                        _authEventsChannel.emit(EventState.Error(it.message.toString()))
                    }
                    is Resource.Loading -> _authEventsChannel.emit(EventState.Loading())
                }
            }
        }

    fun signOut() = ioScope.launch {
        sessionUseCases.signOutUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    _authEventsChannel.emit(EventState.Success(it.data))
                }
                is Resource.Error -> {
                    _authEventsChannel.emit(EventState.Error(it.message.toString()))
                }
                is Resource.Loading -> _authEventsChannel.emit(EventState.Loading())
            }
        }
    }

    /*
    fun getUserUidFromCache() = viewModelScope.launch(coroutineDispatchers.io()) {
        sessionUseCases.getUserUidUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    if (it.data?.isBlank() == false) {
                        _authEventsChannel.emit(EventState.Success("Successfully fetched the user."))
                    }
                }
                is Resource.Error -> {
                    _authEventsChannel.emit(EventState.Error(it.message.toString()))
                }
                is Resource.Loading -> _authEventsChannel.emit(EventState.Loading())
            }
        }
    }

     */

    fun sendPasswordResetEmail(email: String) =
        ioScope.launch {
            authUseCases.sendResetPasswordUseCase.invoke(email).collect {
                when (it) {
                    is Resource.Success -> {
                        _authEventsChannel.emit(EventState.Success(it.data))
                    }
                    is Resource.Error -> {
                        _authEventsChannel.emit(EventState.Error(it.message.toString()))
                    }
                    is Resource.Loading -> _authEventsChannel.emit(EventState.Loading())
                }
            }
        }

}