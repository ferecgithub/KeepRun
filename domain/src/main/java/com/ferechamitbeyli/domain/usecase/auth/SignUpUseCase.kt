package com.ferechamitbeyli.domain.usecase.auth

import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String , password: String, username: String) = authRepository.signUpWithEmailPassword(email, password, username)
}