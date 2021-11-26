package com.ferechamitbeyli.presentation.utils.helpers

import android.util.Patterns
import com.ferechamitbeyli.presentation.utils.enums.ValidationErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults
import com.ferechamitbeyli.presentation.utils.states.ValidationState
import java.util.regex.Pattern

object ValidationHelperFunctions {

    // Password Pattern for Validation
    private val PASSWORD_PATTERN: Pattern =
        Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&+-=])" + "(?=\\S+$)" + ".{6,}" + "$")

    /**
     * Validation Helper Functions
     */

    fun validateUsername(input: String): ValidationState {
        val userName = input.trim()
        return when {
            userName.isBlank() -> {
                ValidationState.ValidationError(ValidationErrorResults.EMPTY_USERNAME)
            }
            userName.length > 12 -> {
                ValidationState.ValidationError(ValidationErrorResults.USERNAME_TOO_LONG)
            }
            else -> {
                ValidationState.ValidationSuccess(ValidationResults.VALID_USERNAME)
            }
        }
    }


    fun validateEmail(input: String): ValidationState {
        val userEmail = input.trim()
        return when {
            userEmail.isBlank() -> {
                ValidationState.ValidationError(ValidationErrorResults.EMPTY_EMAIL)
            }
            !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() -> {
                ValidationState.ValidationError(ValidationErrorResults.INVALID_EMAIL)
            }
            else -> {
                ValidationState.ValidationSuccess(ValidationResults.VALID_EMAIL)
            }
        }
    }

    fun validatePasswords(password: String, confirmPassword: String): ValidationState {
        val pass = password.trim()
        val confirmPass = confirmPassword.trim()

        return when {
            (pass.isBlank()) -> {
                ValidationState.ValidationError(ValidationErrorResults.EMPTY_PASSWORD)
            }
            (confirmPass.isBlank()) -> {
                ValidationState.ValidationError(ValidationErrorResults.EMPTY_CONFIRM_PASSWORD)
            }
            (pass != confirmPass || confirmPass != pass) -> {
                ValidationState.ValidationError(ValidationErrorResults.PASSWORDS_NOT_MATCHED)
            }
            (!PASSWORD_PATTERN.matcher(pass)
                .matches() && !PASSWORD_PATTERN.matcher(confirmPass).matches()) -> {
                ValidationState.ValidationError(ValidationErrorResults.PASSWORD_CHAR_ERROR)
            }
            else -> {
                ValidationState.ValidationSuccess(ValidationResults.VALID_BOTH_PASSWORDS)
            }
        }
    }

    fun validatePassword(input: String): ValidationState {
        val pass = input.trim()
        return when {
            pass.isBlank() -> {
                ValidationState.ValidationError(ValidationErrorResults.EMPTY_PASSWORD)
            }
            else -> {
                ValidationState.ValidationSuccess(ValidationResults.VALID_PASSWORD)
            }
        }
    }


}


