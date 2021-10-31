package com.ferechamitbeyli.domain.usecase.auth.signup

import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.AuthRepository
import javax.inject.Inject

class CreateUserInRemoteDBUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: User) = authRepository.createUserInRemoteDB(user)
}