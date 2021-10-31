package com.ferechamitbeyli.presentation.utils.states

sealed class EventState {
    data class Success(var message: String? = null) : EventState()
    data class Error(var message: String) : EventState()
    data class Loading(val message: String? = null) : EventState()
}