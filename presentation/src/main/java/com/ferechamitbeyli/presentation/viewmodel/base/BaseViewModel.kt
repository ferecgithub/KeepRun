package com.ferechamitbeyli.presentation.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    protected val uiScope = CoroutineScope(
        coroutineDispatchers.ui() + SupervisorJob()
    )

    protected val ioScope = CoroutineScope(
        coroutineDispatchers.io() + SupervisorJob()
    )

    protected val defaultScope = CoroutineScope(
        coroutineDispatchers.default() + SupervisorJob()
    )

    var networkState = networkConnectionTracker.asFlow()

    override fun onCleared() {
        super.onCleared()
        uiScope.coroutineContext.cancelChildren()
        ioScope.coroutineContext.cancelChildren()
        defaultScope.coroutineContext.cancelChildren()
    }
}