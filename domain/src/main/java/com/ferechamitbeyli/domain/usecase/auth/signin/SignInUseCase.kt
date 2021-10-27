package com.ferechamitbeyli.domain.usecase.auth.signin

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<String>> =
        authRepository.signInWithEmailPassword(email, password)
}