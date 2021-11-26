package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity

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
class HomeViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    /**
     * Fetches and stores the required permissions of the application in cache
     */

    fun storeFineLocationPermissionState(hasPermission: Boolean) =
        ioScope.launch {
            sessionUseCases.cacheFineLocationPermissionStateUseCase.invoke(hasPermission)
        }

    fun getFineLocationPermissionState() = flow<Boolean> {
        sessionUseCases.getFineLocationPermissionStateUseCase.invoke().collect {
            emit(it)
        }
    }

    fun storeActivityRecognitionPermissionState(hasPermission: Boolean) =
        ioScope.launch {
            sessionUseCases.cacheActivityRecognitionPermissionStateUseCase.invoke(hasPermission)
        }

    fun getActivityRecognitionPermissionState() = flow<Boolean> {
        sessionUseCases.getActivityRecognitionPermissionStateUseCase.invoke().collect {
            emit(it)
        }
    }

}