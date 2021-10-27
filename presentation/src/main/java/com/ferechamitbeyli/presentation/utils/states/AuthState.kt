package com.ferechamitbeyli.presentation.utils.states

import com.ferechamitbeyli.presentation.utils.enums.ConnectionErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults

sealed class AuthState {
    data class Success(var result: ValidationResults) : AuthState()
    data class ValidationError(val result: ValidationErrorResults) : AuthState()
    data class Error(val result: ConnectionErrorResults) : AuthState()
    object Loading : AuthState()
}
