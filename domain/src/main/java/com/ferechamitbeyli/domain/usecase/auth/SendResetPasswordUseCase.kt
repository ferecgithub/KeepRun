package com.ferechamitbeyli.domain.usecase.auth

import com.ferechamitbeyli.domain.repository.AuthRepository

class SendResetPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) = authRepository.sendResetPassword(email)
}