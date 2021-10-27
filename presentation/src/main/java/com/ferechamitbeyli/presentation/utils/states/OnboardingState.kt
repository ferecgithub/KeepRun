package com.ferechamitbeyli.presentation.utils.states

import com.ferechamitbeyli.presentation.utils.enums.SessionResults

sealed class OnboardingState {
    data class Success(var result: SessionResults) : OnboardingState()
    data class Error(val result: String) : OnboardingState()
    object Loading : OnboardingState()
}