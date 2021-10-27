package com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.fragments.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.presentation.utils.enums.SessionResults
import com.ferechamitbeyli.presentation.utils.states.OnboardingState
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val useCases: SessionUseCases,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    /**
     * Data and functions related to if onboarding/walkthrough screen finished or not
     */

    private var _onboardingEventsFlow = MutableSharedFlow<OnboardingState>()
    val onboardingEventsFlow: MutableSharedFlow<OnboardingState> = _onboardingEventsFlow

    /**
     * Checks and stores the first use state of the application in cache
     */
    private var _firstUseState = MutableSharedFlow<Boolean?>()
    val firstUseState: MutableSharedFlow<Boolean?> = _firstUseState

    fun storeFirstUseState(isFinished: Boolean) =
        viewModelScope.launch(coroutineDispatchers.io()) {
            useCases.cacheFirstUseStateUseCase.invoke(isFinished)
            _onboardingEventsFlow.emit(OnboardingState.Success(SessionResults.FIRST_USE_STATE_STORED))
            //_firstUseState.emit(isFinished)
        }

    fun getFirstUseState() = viewModelScope.launch(coroutineDispatchers.io()) {
        useCases.getFirstUseStateUseCase.invoke().collect {
            if (it.data == true) {
                _onboardingEventsFlow.emit(OnboardingState.Success(SessionResults.FIRST_USE))
            } else {
                _onboardingEventsFlow.emit(OnboardingState.Success(SessionResults.NOT_FIRST_USE))
            }
            //_firstUseState.emit(it.data)
        }
    }

    /*
    /**
     * Checks the initial setup state of home screen from cache.
     * If true, this means the user already completed onboarding,
     * and entered weight information on InitialFragment.
     */
    private var _initialSetupState = MutableSharedFlow<Boolean?>()
    val initialSetupState: MutableSharedFlow<Boolean?> = _initialSetupState

    suspend fun getInitialSetupState() = viewModelScope.launch(coroutineDispatchers.io()) {
        onboardingUseCases.getInitialSetupStateUseCase.invoke().collect {
            if (it.data == true) {
                _onboardingEventsFlow.emit(OnboardingState.Success(SessionResults.INITIAL_SETUP_DONE))
            } else {
                _onboardingEventsFlow.emit(OnboardingState.Success(SessionResults.INITIAL_SETUP_NOT_DONE))
            }
            //_initialSetupState.emit(it.data)
        }
    }

     */

    /**
     * Fetches current user from cache. Currently, it is fetched from FirebaseAuth.
     */
    private var _currentUser = MutableSharedFlow<User?>()
    val currentUser: MutableSharedFlow<User?> = _currentUser

    fun getCurrentUser() = viewModelScope.launch(coroutineDispatchers.io()) {
        useCases.getCurrentUserUseCase.invoke().collect {
            if (it.data != null) {
                _onboardingEventsFlow.emit(OnboardingState.Success(SessionResults.THERE_IS_A_CURRENT_USER))
            } else {
                _onboardingEventsFlow.emit(OnboardingState.Success(SessionResults.THERE_IS_NOT_A_CURRENT_USER))
            }
            //_currentUser.emit(it.data)
        }
    }
}