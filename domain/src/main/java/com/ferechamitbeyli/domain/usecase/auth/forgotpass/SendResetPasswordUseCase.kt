package com.ferechamitbeyli.domain.usecase.auth.forgotpass

import com.ferechamitbeyli.domain.repository.AuthRepository
import javax.inject.Inject

class SendResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) = authRepository.sendResetPassword(email)
}