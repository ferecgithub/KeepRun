package com.ferechamitbeyli.presentation.utils.usecases

import com.ferechamitbeyli.domain.usecase.auth.forgotpass.SendResetPasswordUseCase
import com.ferechamitbeyli.domain.usecase.auth.signin.SignInUseCase
import com.ferechamitbeyli.domain.usecase.auth.signup.CreateUserInRemoteDBUseCase
import com.ferechamitbeyli.domain.usecase.auth.signup.SignUpUseCase
import javax.inject.Inject

data class AuthUseCases @Inject constructor(
    val signUpUseCase: SignUpUseCase,
    val createUserInRemoteDBUseCase: CreateUserInRemoteDBUseCase,
    val signInUseCase: SignInUseCase,
    val sendResetPasswordUseCase: SendResetPasswordUseCase
)