package com.ferechamitbeyli.presentation.utils

import android.util.Patterns
import com.ferechamitbeyli.presentation.utils.enums.ValidationErrorResults
import com.ferechamitbeyli.presentation.utils.enums.ValidationResults
import com.ferechamitbeyli.presentation.utils.states.AuthState
import java.util.regex.Pattern

object ValidationHelperFunctions {

    // Password Pattern for Validation
    private val PASSWORD_PATTERN: Pattern =
        Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&+-=])" + "(?=\\S+$)" + ".{6,}" + "$")

    /**
     * Validation Helper Functions
     */
/*
fun validateUsername(context: Context, input: String, editText: TextInputEditText) : Boolean {
    val userName = input.trim()
    return when {
        userName.isEmpty() -> {
            editText.error = context.getString(R.string.fields_cant_be_empty)
            false
        }
        userName.length > 15 -> {
            editText.error = context.getString(R.string.username_too_long)
            false
        }
        else -> {
            editText.error = null
            true
        }
    }
}
 */

    fun validateUsername(input: String): AuthState {
        val userName = input.trim()
        return when {
            userName.isEmpty() -> {
                AuthState.ValidationError(ValidationErrorResults.EMPTY_USERNAME)
            }
            userName.length > 12 -> {
                AuthState.ValidationError(ValidationErrorResults.LENGTH_TOO_LONG)
            }
            else -> {
                AuthState.Success(ValidationResults.VALID_USERNAME)
            }
        }
    }


/*
fun validateEmail(context: Context, input: String, editText: TextInputEditText) : Boolean {
    val userEmail = input.trim()
    return when {
        userEmail.isEmpty() -> {
            editText.error = context.getString(R.string.fields_cant_be_empty)
            false
        }
        !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() -> {
            editText.error = "Invalid email address"
            false
        }
        else -> {
            editText.error = null
            true
        }
    }
}
 */

    fun validateEmail(input: String): AuthState {
        val userEmail = input.trim()
        return when {
            userEmail.isEmpty() -> {
                AuthState.ValidationError(ValidationErrorResults.EMPTY_EMAIL)
            }
            !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() -> {
                AuthState.ValidationError(ValidationErrorResults.INVALID_EMAIL)
            }
            else -> {
                AuthState.Success(ValidationResults.VALID_EMAIL)
            }
        }
    }

/*
fun validatePassword(context: Context, password: String, confirmPassword: String, passwordEt: TextInputEditText, confirmPasswordEt: TextInputEditText) : Boolean {
    val pass = password.trim()
    val confirmPass = confirmPassword.trim()

    if (pass.isNotEmpty() && confirmPass.isNotEmpty()) {
        return when {
            (pass != confirmPass && confirmPass != pass) -> {
                passwordEt.error = context.getString(R.string.please_enter_matches_pass)
                confirmPasswordEt.error = context.getString(R.string.please_enter_matches_pass)
                false
            }
            (!Constants.PASSWORD_PATTERN.matcher(pass).matches() && !Constants.PASSWORD_PATTERN.matcher(confirmPass).matches()) -> {
                passwordEt.error = context.getString(R.string.password_requires)
                confirmPasswordEt.error = context.getString(R.string.password_requires)
                false
            }
            else -> {
                passwordEt.error = null
                confirmPasswordEt.error = null
                true
            }
        }
    }
    return true
}
 */

    fun validatePasswords(password: String, confirmPassword: String): AuthState {
        val pass = password.trim()
        val confirmPass = confirmPassword.trim()

        if (pass.isEmpty() or confirmPass.isEmpty()) {
            ValidationErrorResults.EMPTY_PASSWORD
        } else if (pass.isNotEmpty() && confirmPass.isNotEmpty()) {
            return when {
                (pass != confirmPass && confirmPass != pass) -> {
                    AuthState.ValidationError(ValidationErrorResults.PASSWORDS_NOT_MATCHED)
                }
                (!PASSWORD_PATTERN.matcher(pass)
                    .matches() && !PASSWORD_PATTERN.matcher(confirmPass).matches()) -> {
                    AuthState.ValidationError(ValidationErrorResults.PASSWORD_CHAR_ERROR)
                }
                else -> {
                    AuthState.Success(ValidationResults.VALID_CONFIRM_PASSWORD)
                }
            }
        }

        return AuthState.Success(ValidationResults.VALID_CONFIRM_PASSWORD)
    }

    fun validatePassword(input: String): AuthState {
        val pass = input.trim()
        return when {
            pass.isEmpty() -> {
                AuthState.ValidationError(ValidationErrorResults.EMPTY_PASSWORD)
            }
            else -> {
                AuthState.Success(ValidationResults.VALID_PASSWORD)
            }
        }
    }
}


