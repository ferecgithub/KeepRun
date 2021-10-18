package com.ferechamitbeyli.domain.usecase.auth

import com.ferechamitbeyli.domain.repository.AuthRepository

class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.signOut()
}