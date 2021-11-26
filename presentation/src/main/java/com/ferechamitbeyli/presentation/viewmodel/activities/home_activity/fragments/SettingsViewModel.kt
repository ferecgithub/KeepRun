package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.helpers.ValidationHelperFunctions.validatePasswords
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.states.ValidationState
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    private var _settingsValidationEventsChannel = MutableSharedFlow<ValidationState>()
    val settingsValidationEventsChannel: SharedFlow<ValidationState> =
        _settingsValidationEventsChannel

    private var _settingsEventsChannel = MutableSharedFlow<EventState>()
    val settingsEventsChannel: SharedFlow<EventState> = _settingsEventsChannel

    fun getUsernameFromCache() = flow<String> {
        sessionUseCases.getUsernameUseCase.invoke().collect {
            emit(it)
        }
    }

    fun getUserEmailFromCache() = flow<String> {
        sessionUseCases.getUserEmailUseCase.invoke().collect {
            emit(it)
        }

    }

    fun getUserWeightFromCache() = flow<Double> {
        sessionUseCases.getUserWeightUseCase.invoke().collect {
            emit(it)
        }
    }

    fun getUserNotificationStateFromCache() = flow<Boolean> {
        sessionUseCases.getUserNotificationStateUseCase.invoke().collect {
            emit(it)
        }
    }

    fun validatePasswordsBeforeUpdating(
        password: String,
        confirmPass: String,
    ) = defaultScope.launch {
        val passwordsValidation = validatePasswords(password, confirmPass)

        _settingsValidationEventsChannel.emit(passwordsValidation)
    }

    fun savePasswordToUserProfile(
        password: String
    ) = ioScope.launch {
        sessionUseCases.updateUserPasswordUseCase.invoke(password).collect { passwordResponse ->
            when (passwordResponse) {
                is Resource.Error -> {
                    _settingsEventsChannel.emit(EventState.Error("An error occurred while updating password."))
                }
                is Resource.Loading -> {
                    _settingsEventsChannel.emit(EventState.Loading())
                }
                is Resource.Success -> {
                    _settingsEventsChannel.emit(EventState.Success("Your password is successfully updated."))
                }
            }
        }
    }

    fun saveAllFieldsOtherThanPasswordToRemoteDB(
        notificationState: Boolean,
        weight: Double
    ) = ioScope.launch {
        combine(
            sessionUseCases.updateUserNotificationStateUseCase.invoke(notificationState),
            sessionUseCases.updateUserWeightToRemoteDBUseCase.invoke(weight)
        ) { notificationResponse, weightResponse ->
            if (notificationResponse is Resource.Loading || weightResponse is Resource.Loading) {
                _settingsEventsChannel.emit(EventState.Loading())
            }
            if (notificationResponse is Resource.Error || weightResponse is Resource.Error) {
                _settingsEventsChannel.emit(EventState.Error("An error occurred while updating information."))
            }
            if (notificationResponse is Resource.Success && weightResponse is Resource.Success) {
                _settingsEventsChannel.emit(EventState.Success("Your information is successfully updated."))
            }

        }.collect()

    }


    fun resetCachedUser() = ioScope.launch {
        sessionUseCases.resetCachedUserUseCase.invoke()
    }

    fun logout() = ioScope.launch {
        sessionUseCases.signOutUseCase.invoke()
    }
}