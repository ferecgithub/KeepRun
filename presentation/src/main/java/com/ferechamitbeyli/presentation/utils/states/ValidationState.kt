package com.ferechamitbeyli.presentation.utils.states

import com.ferechamitbeyli.presentation.utils.enums.ValidationErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults

sealed class ValidationState {
    data class ValidationSuccess(var result: ValidationResults) : ValidationState()
    data class ValidationError(val result: ValidationErrorResults) : ValidationState()
}
