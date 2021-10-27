package com.ferechamitbeyli.domain.usecase.auth.signup

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String
    ): Flow<Resource<String>> = authRepository.signUp(email, password, username)
}