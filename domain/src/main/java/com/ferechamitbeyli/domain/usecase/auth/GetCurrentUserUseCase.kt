package com.ferechamitbeyli.domain.usecase.auth

import com.ferechamitbeyli.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.getCurrentUser()
}