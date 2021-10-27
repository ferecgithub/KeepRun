package com.ferechamitbeyli.domain.usecase.auth.signout

import com.ferechamitbeyli.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.signOut()
}