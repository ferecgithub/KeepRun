package com.ferechamitbeyli.keeprun.viewmodel.activities.auth_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel  @Inject constructor() : ViewModel() {

    private var _stepNr = MutableLiveData<Int>(0)
    val stepNr : LiveData<Int> = _stepNr


    fun assignStepNr(step: Int) = viewModelScope.launch {
        _stepNr.postValue(step)
    }



}