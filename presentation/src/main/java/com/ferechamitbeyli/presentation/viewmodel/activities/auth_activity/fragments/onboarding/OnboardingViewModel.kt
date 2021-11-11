package com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.fragments.onboarding

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.uimodels.UserUIModel
import com.ferechamitbeyli.presentation.uimodels.mappers.UserToUIMapper
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
    private var _firstUseState = MutableStateFlow<Boolean>(true)
    val firstUseState: StateFlow<Boolean> = _firstUseState

    fun storeFirstUseState(isFinished: Boolean) =
        ioScope.launch {
            sessionUseCases.cacheFirstUseStateUseCase.invoke(isFinished)
            _firstUseState.emit(isFinished)
        }

    fun getFirstUseState() = ioScope.launch {
        sessionUseCases.getFirstUseStateUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    _firstUseState.emit(it.data == true)
                        .also { _onboardingEventsFlow.emit(EventState.Success()) }
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

    private var _userEmailFlow = MutableStateFlow<String>("")
    val userEmailFlow: StateFlow<String> = _userEmailFlow

    fun getUserEmailFromCache() = ioScope.launch {
        sessionUseCases.getUserEmailUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    _userEmailFlow.emit(it.data.toString())
                }
                is Resource.Error -> {
                    _onboardingEventsFlow.emit(EventState.Error(it.message.toString()))
                }
                is Resource.Loading -> _onboardingEventsFlow.emit(EventState.Loading())
            }
        }
    }

    /**
     * Fetches current user from Firebase Realtime DB.
     */

    private var _userFromRemoteDBFlow = MutableStateFlow<UserUIModel>(UserUIModel())
    val userFromRemoteDBFlow: StateFlow<UserUIModel> = _userFromRemoteDBFlow

    fun getCurrentUserFromRemoteDB() = ioScope.launch {
        sessionUseCases.getCurrentUserIdentifierUseCase.invoke().collect {
            when (it) {
                is Resource.Success -> {
                    if (!it.data.isNullOrBlank()) {
                        sessionUseCases.getCurrentUserFromRemoteDBUseCase.invoke(it.data!!)
                            .collect { user ->
                                when (user) {
                                    is Resource.Success -> {
                                        _userFromRemoteDBFlow.emit(
                                            UserToUIMapper.mapFromDomainModel(
                                                user.data!!
                                            )
                                        )
                                    }
                                    is Resource.Error -> {
                                        /** NO-OP **/
                                        //_onboardingEventsFlow.emit(EventState.Error(user.message.toString()))
                                    }
                                    is Resource.Loading -> _onboardingEventsFlow.emit(EventState.Loading())
                                }
                            }
                    }
                }
                is Resource.Error -> {
                    _onboardingEventsFlow.emit(EventState.Error(it.message.toString()))
                }
                is Resource.Loading -> _onboardingEventsFlow.emit(EventState.Loading())
            }


        }

    }

    fun cacheCurrentUser(user: UserUIModel) = ioScope.launch {
        sessionUseCases.cacheUserAccountUseCase.invoke(
            user.uid.toString(),
            user.username.toString(),
            user.email.toString(),
            user.weight,
            user.isNotificationEnable,
            "" //user.photoUrl.toString()
        )
    }

}