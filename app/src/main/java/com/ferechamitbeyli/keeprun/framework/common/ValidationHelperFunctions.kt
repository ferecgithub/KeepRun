package com.ferechamitbeyli.keeprun.framework.common

import android.util.Patterns
import com.ferechamitbeyli.keeprun.framework.model.remote.enitities.enums.ValidationResults

object ValidationHelperFunctions {

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

    fun validateUsername(input: String): ValidationResults {
        val userName = input.trim()
        return when {
            userName.isEmpty() -> {
                ValidationResults.EMPTY_USERNAME
            }
            userName.length > 15 -> {
                ValidationResults.LENGTH_TOO_LONG
            }
            else -> {
                ValidationResults.SUCCESS
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

    fun validateEmail(input: String): ValidationResults {
        val userEmail = input.trim()
        return when {
            userEmail.isEmpty() -> {
                ValidationResults.EMPTY_EMAIL
            }
            !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() -> {
                ValidationResults.INVALID_EMAIL
            }
            else -> {
                ValidationResults.SUCCESS
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

    fun validatePasswords(password: String, confirmPassword: String): ValidationResults {
        val pass = password.trim()
        val confirmPass = confirmPassword.trim()

        if (pass.isEmpty() or confirmPass.isEmpty()) {
            ValidationResults.EMPTY_PASSWORD
        } else if (pass.isNotEmpty() && confirmPass.isNotEmpty()) {
            return when {
                (pass != confirmPass && confirmPass != pass) -> {
                    ValidationResults.PASSWORDS_NOT_MATCHED
                }
                (!Constants.PASSWORD_PATTERN.matcher(pass)
                    .matches() && !Constants.PASSWORD_PATTERN.matcher(confirmPass).matches()) -> {
                    ValidationResults.PASSWORD_CHAR_ERROR
                }
                else -> {
                    ValidationResults.SUCCESS
                }
            }
        }
        return ValidationResults.SUCCESS
    }

    fun validatePassword(input: String): ValidationResults {
        val pass = input.trim()
        return when {
            pass.isEmpty() -> {
                ValidationResults.EMPTY_PASSWORD
            }
            else -> {
                ValidationResults.SUCCESS
            }
        }
    }
}


