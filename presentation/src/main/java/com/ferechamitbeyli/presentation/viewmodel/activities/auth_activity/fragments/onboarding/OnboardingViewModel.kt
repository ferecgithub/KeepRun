package com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.fragments.onboarding

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    coroutineDispatchers: CoroutineDispatchers,
    networkConnectionTracker: NetworkConnectionTracker
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    /**
     * Data and function related to detect current onboarding pager position
     */

    private var _currentPagerPosition = MutableStateFlow(0)
    val currentPagerPosition: StateFlow<Int> = _currentPagerPosition

    fun assignPagerPosition(currentPosition: Int) = defaultScope.launch {
        _currentPagerPosition.emit(currentPosition)
    }

    /**
     * Data and functions related to detect if onboarding screen is finished or not
     */


    private var _onboardingEventsFlow = MutableSharedFlow<EventState>()
    val onboardingEventsFlow: SharedFlow<EventState> = _onboardingEventsFlow


    /**
     * Checks and stores the first use state of the application in cache
     */

    fun storeFirstUseState(isFinished: Boolean) =
        ioScope.launch {
            sessionUseCases.cacheFirstUseStateUseCase.invoke(isFinished)
        }

    fun getFirstUseState() = flow<Boolean> {
        sessionUseCases.getFirstUseStateUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { state -> emit(state) }
                }
                is Resource.Error -> {
                    _onboardingEventsFlow.emit(EventState.Error(it.message.toString()))
                }
                is Resource.Loading -> _onboardingEventsFlow.emit(EventState.Loading())
            }
        }
    }

    /**
     * Fetches current user from cache.
     */

    fun getUserEmailFromCache() = flow<String> {
        sessionUseCases.getUserEmailUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { email -> emit(email) }
                }
                is Resource.Error -> {
                    _onboardingEventsFlow.emit(EventState.Error(it.message.toString()))
                }
                is Resource.Loading -> _onboardingEventsFlow.emit(EventState.Loading())
            }
        }
    }

}