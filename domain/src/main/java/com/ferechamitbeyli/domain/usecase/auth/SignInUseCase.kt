package com.ferechamitbeyli.domain.usecase.auth

import com.ferechamitbeyli.domain.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String , password: String) = authRepository.signInWithEmailPassword(email, password)
}