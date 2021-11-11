package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    //private val homeUseCases: HomeUseCases,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    private var _userUidFlow = MutableStateFlow<String>("")
    val userUidFlow: StateFlow<String> = _userUidFlow

    fun getUserUidFromCache() = ioScope.launch {
        sessionUseCases.getUserUidUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    _userUidFlow.emit(it.data.toString())
                }
                is Resource.Error -> {
                    /** NO-OP **/
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    private var _usernameFlow = MutableStateFlow<String>("")
    val usernameFlow: StateFlow<String> = _usernameFlow

    fun getUsernameFromCache() = ioScope.launch {
        sessionUseCases.getUsernameUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    _usernameFlow.emit(capitalizeFirstLetter(it.data.toString()))
                }
                is Resource.Error -> {
                    /** NO-OP **/
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    private var _userWeightFlow = MutableStateFlow<Double>(0.0)
    val userWeightFlow: StateFlow<Double> = _userWeightFlow

    fun getUserWeightFromRemoteDB() = ioScope.launch {
        sessionUseCases.getUserWeightFromRemoteDBUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { weight -> _userWeightFlow.emit(weight) }
                }
                is Resource.Error -> {
                    /** NO-OP **/
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    private var _userMailFlow = MutableStateFlow<String>("")
    val userMailFlow: StateFlow<String> = _userMailFlow

    fun getUserMailFromCache() = ioScope.launch {
        sessionUseCases.getUserEmailUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    _userMailFlow.emit(it.data.toString())
                }
                is Resource.Error -> {
                    /** NO-OP **/
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    private var _userNotificationStateFlow = MutableStateFlow<Boolean>(true)
    val userNotificationStateFlow: StateFlow<Boolean> = _userNotificationStateFlow

    fun getUserNotificationStateFromCache() = ioScope.launch {
        sessionUseCases.getUserNotificationStateUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { notificationState ->
                        _userNotificationStateFlow.emit(
                            notificationState
                        )
                    }
                }
                is Resource.Error -> {
                    /** NO-OP **/
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    private var _userPhotoUrlFlow = MutableStateFlow<String>("")
    val userPhotoUrlFlow: StateFlow<String> = _userPhotoUrlFlow

    fun getUserPhotoUrlFromCache() = ioScope.launch {
        sessionUseCases.getUserPhotoUrlUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    _userPhotoUrlFlow.emit(it.data.toString())
                }
                is Resource.Error -> {
                    /** NO-OP **/
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }


}