package com.ferechamitbeyli.keeprun.viewmodel.activities.auth_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferechamitbeyli.keeprun.model.local.cache.DataStoreObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel  @Inject constructor() : ViewModel() {

    @Inject
    lateinit var dataStoreObject: DataStoreObject

    private var _isFirstUse = MutableLiveData<Boolean>(true)
    val isFirstUse : LiveData<Boolean> = _isFirstUse


    fun assignFirstUse(isFirstUse: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreObject.storeIfFirstTimeUse(isFirstUse)
        _isFirstUse.postValue(isFirstUse)
    }

    fun getIfFirstUse() = viewModelScope.launch(Dispatchers.IO) {
        dataStoreObject.getIsFirstTime().collect {
            _isFirstUse.postValue(it)
        }
    }





}