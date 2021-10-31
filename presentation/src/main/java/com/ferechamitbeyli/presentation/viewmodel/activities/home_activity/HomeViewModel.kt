package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity

import androidx.lifecycle.ViewModel
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.ferechamitbeyli.presentation.viewmodel.activities.auth_activity.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: BaseAuthRepository,
    private val dataStoreObject: DataStoreObject
) : ViewModel() {

    private val TAG = "HomeViewModel"

    /**
     * Checking Internet Connection of App
     */

    private var _isOnline = MutableLiveData<Boolean>(true)
    val isOnline: LiveData<Boolean> = _isOnline

    fun getIsOnline(@ApplicationContext context: Context) = viewModelScope.launch(Dispatchers.Default) {
        val isOnline = isOnline(context)
        _isOnline.postValue(isOnline)
    }

    /**
     * First Use of Application (
     * First use data (boolean) and related functions
     */

    private var _isFirstUse = MutableLiveData<Boolean>(true)
    val isFirstUse: LiveData<Boolean> = _isFirstUse


    fun assignFirstUse(isFirstUse: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreObject.storeIfFirstTimeUse(isFirstUse)
        _isFirstUse.postValue(isFirstUse)
    }

    fun getIfFirstUse() = viewModelScope.launch(Dispatchers.IO) {
        dataStoreObject.getIsFirstTime().collect {
            _isFirstUse.postValue(it)
        }
    }

    /**
     * Firebase Authentication Functions
     */

    private val _eventsChannel = Channel<AuthViewModel.AuthEvents>()
    val eventsChannel = _eventsChannel.receiveAsFlow()

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = _firebaseUser

    fun signOut() = viewModelScope.launch {
        try {
            val user = authRepository.signOut()
            user?.let {
                _eventsChannel.send(AuthViewModel.AuthEvents.Success("Logout Failure"))
            } ?: _eventsChannel.send(AuthViewModel.AuthEvents.Success("Sign Out Successful"))

            getCurrentUser()

        } catch (e: Exception) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signInUser: ${error[1]}")
            _eventsChannel.send(AuthViewModel.AuthEvents.Error(error[1]))
        }
    }

    fun getCurrentUser() = viewModelScope.launch {
        val user = authRepository.getCurrentUser()
        _firebaseUser.postValue(user)
    }






}

 */

@HiltViewModel
class HomeViewModel @Inject constructor(networkConnectionTracker: NetworkConnectionTracker, coroutineDispatchers: CoroutineDispatchers) :
    BaseViewModel(networkConnectionTracker, coroutineDispatchers)