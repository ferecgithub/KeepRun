package com.ferechamitbeyli.domain.usecase.auth

import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.AuthRepository

class CreateUserInRemoteDBUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: User, any: Any) = authRepository.createUserInRemoteDB(user, any)
}