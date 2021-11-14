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
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {


    /*
    private var _initialEventsFlow = MutableSharedFlow<EventState>()
    val initialEventsFlow: SharedFlow<EventState> = _initialEventsFlow

     */

    fun getUsernameFromRemoteDB() = flow<String> {
        sessionUseCases.getUsernameFromRemoteDBUseCaseUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { username -> emit(capitalizeFirstLetter(username)) }
                    logcat("USERNAME_SUCCESS") { it.data.toString() }
                    println("USERNAME_SUCCESS ${it.data.toString()} ")
                }
                is Resource.Error -> {
                    logcat("USERNAME_ERROR") { "USERNAME_ERROR" }
                    println("USERNAME_ERROR ${it.message.toString()} ")
                    /** NO-OP **/
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    fun getUserWeightFromRemoteDB() = flow<Double> {
        sessionUseCases.getUserWeightFromRemoteDBUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { weight -> emit(weight) }
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

    fun getUserWeightFromCache() = flow<Double> {
        sessionUseCases.getUserWeightUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { weight -> emit(weight) }
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

    fun saveWeightInformation(weight: Double) = ioScope.launch {
        sessionUseCases.updateUserWeightToRemoteDBUseCase.invoke(weight).collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                        sessionUseCases.cacheUserWeightUseCase.invoke(weight)
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

}