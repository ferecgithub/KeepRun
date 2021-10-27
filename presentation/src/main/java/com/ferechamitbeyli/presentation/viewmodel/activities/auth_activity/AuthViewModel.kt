package com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity

import androidx.lifecycle.*
import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.utils.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.ValidationHelperFunctions.validateEmail
import com.ferechamitbeyli.presentation.utils.ValidationHelperFunctions.validatePassword
import com.ferechamitbeyli.presentation.utils.ValidationHelperFunctions.validatePasswords
import com.ferechamitbeyli.presentation.utils.ValidationHelperFunctions.validateUsername
import com.ferechamitbeyli.presentation.utils.enums.ConnectionErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults
import com.ferechamitbeyli.presentation.utils.states.AuthState
import com.ferechamitbeyli.presentation.utils.usecases.AuthUseCases
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val sessionUseCases: SessionUseCases,
    private val coroutineDispatchers: CoroutineDispatchers,
    //private val networkConnectionTracker: NetworkConnectionTracker
) : ViewModel() {

    @Inject
    lateinit var networkConnectionTracker: NetworkConnectionTracker

    private var _authEventsChannel = MutableSharedFlow<AuthState>()
    val authEventsFlow: MutableSharedFlow<AuthState> = _authEventsChannel

    private var _networkState = MutableStateFlow<Boolean>(true)
    val networkState: MutableSharedFlow<Boolean> = _networkState

    init {
        _networkState.value = networkConnectionTracker.value ?: false
    }

    fun validateBeforeSignIn(email: String, password: String) =
        viewModelScope.launch(coroutineDispatchers.default()) {
            val emailValidation = validateEmail(email)
            val passwordValidation = validatePassword(password)

            when {
                (emailValidation != AuthState.Success(ValidationResults.VALID_EMAIL)) -> {
                    _authEventsChannel.emit(emailValidation)
                }
                (passwordValidation != AuthState.Success(ValidationResults.VALID_PASSWORD)) -> {
                    _authEventsChannel.emit(passwordValidation)
                }
                else -> {
                    signInUser(email, password)
                }
            }
        }

    fun validateBeforeSignUp(
        email: String,
        password: String,
        confirmPass: String,
        username: String
    ) =
        viewModelScope.launch(coroutineDispatchers.default()) {
            val emailValidation = validateEmail(email)
            val passwordsValidation = validatePasswords(password, confirmPass)
            val usernameValidation = validateUsername(username)

            when {
                (emailValidation != AuthState.Success(ValidationResults.VALID_EMAIL)) -> {
                    _authEventsChannel.emit(emailValidation)
                }
                (passwordsValidation != AuthState.Success(ValidationResults.VALID_CONFIRM_PASSWORD)) -> {
                    _authEventsChannel.emit(passwordsValidation)
                }
                (usernameValidation != AuthState.Success(ValidationResults.VALID_USERNAME)) -> {
                    _authEventsChannel.emit(usernameValidation)
                }
                else -> {
                    signUpUser(email, password, username)
                }
            }
        }

    fun verifySendPasswordReset(email: String) =
        viewModelScope.launch(coroutineDispatchers.default()) {
            val emailValidation = validateEmail(email)
            if (emailValidation != AuthState.Success(ValidationResults.VALID_EMAIL)) {
                _authEventsChannel.emit(emailValidation)
            } else {
                sendPasswordResetEmail(email)
            }
        }

    private fun signInUser(email: String, password: String) =
        viewModelScope.launch(coroutineDispatchers.io()) {
            authUseCases.signInUseCase.invoke(email, password).collect {
                when (it) {
                    is Resource.Success -> {
                        _authEventsChannel.emit(AuthState.Success(ValidationResults.SUCCESS_SIGNIN))
                    }
                    is Resource.Error -> {
                        _authEventsChannel.emit(AuthState.Error(ConnectionErrorResults.GENERAL_ERROR))
                    }
                    is Resource.Loading -> _authEventsChannel.emit(AuthState.Loading)
                }
            }
        }

    private fun signUpUser(email: String, password: String, username: String) =
        viewModelScope.launch(coroutineDispatchers.io()) {
            authUseCases.signUpUseCase.invoke(email, password, username).collect {
                when (it) {
                    is Resource.Success -> {
                        _authEventsChannel.emit(AuthState.Success(ValidationResults.SUCCESS_SIGNUP))
                    }
                    is Resource.Error -> {
                        _authEventsChannel.emit(AuthState.Error(ConnectionErrorResults.GENERAL_ERROR))
                    }
                    is Resource.Loading -> _authEventsChannel.emit(AuthState.Loading)
                }
            }
        }

    fun signOut() = viewModelScope.launch(coroutineDispatchers.io()) {
        authUseCases.signOutUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    _authEventsChannel.emit(AuthState.Success(ValidationResults.SUCCESS_SIGNOUT))
                }
                is Resource.Error -> {
                    _authEventsChannel.emit(AuthState.Error(ConnectionErrorResults.GENERAL_ERROR))
                }
                is Resource.Loading -> _authEventsChannel.emit(AuthState.Loading)
            }
        }
    }

    fun getCurrentUser() = viewModelScope.launch(coroutineDispatchers.io()) {
        sessionUseCases.getCurrentUserUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {

                    sessionUseCases.cacheUserAccountUseCase.invoke(
                        it.data?.uid.toString(),
                        it.data?.username.toString(),
                        it.data?.email.toString(),
                        it.data?.photoUrl.toString()
                    )

                    _authEventsChannel.emit(AuthState.Success(ValidationResults.SUCCESS_GOT_USER))
                }
                is Resource.Error -> {
                    _authEventsChannel.emit(AuthState.Error(ConnectionErrorResults.GENERAL_ERROR))
                }
                is Resource.Loading -> _authEventsChannel.emit(AuthState.Loading)
            }
        }
    }


    private fun sendPasswordResetEmail(email: String) =
        viewModelScope.launch(coroutineDispatchers.io()) {
            authUseCases.sendResetPasswordUseCase.invoke(email).collect {
                when (it) {
                    is Resource.Success -> {
                        _authEventsChannel.emit(AuthState.Success(ValidationResults.SUCCESS_SENT_RESET_PASSWORD))
                    }
                    is Resource.Error -> {
                        _authEventsChannel.emit(AuthState.Error(ConnectionErrorResults.GENERAL_ERROR))
                    }
                    is Resource.Loading -> _authEventsChannel.emit(AuthState.Loading)
                }
            }
        }


}