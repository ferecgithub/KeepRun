package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    fun getUsernameFromCache() = flow<String> {
        sessionUseCases.getUsernameUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { username -> emit(username) }
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

    fun getUserEmailFromCache() = flow<String> {
        sessionUseCases.getUserEmailUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { email -> emit(email) }
                    println("EMAIL_CACHE_SUCCESS : ${it.data.toString()}")
                }
                is Resource.Error -> {
                    println("EMAIL_CACHE_ERROR")
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    fun getUserWeightFromCache() = flow<Double> {
        sessionUseCases.getUserWeightUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { weight -> emit(weight) }
                    println("WEIGHT_CACHE_SUCCESS : ${it.data.toString()}")
                }
                is Resource.Error -> {
                    println("WEIGHT_CACHE_ERROR ")
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    fun getUserNotificationStateFromCache() = flow<Boolean> {
        sessionUseCases.getUserNotificationStateUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { isEnabled -> emit(isEnabled) }
                    println("NOTIFICATION_CACHE_SUCCESS : ${it.data.toString()}")
                }
                is Resource.Error -> {
                    println("NOTIFICATION_CACHE_ERROR")
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    fun resetCachedUser() = ioScope.launch {
        sessionUseCases.resetCachedUserUseCase.invoke()
    }

    fun logout() = ioScope.launch {
        sessionUseCases.signOutUseCase.invoke()
    }
}