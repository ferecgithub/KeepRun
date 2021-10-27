package com.ferechamitbeyli.domain.usecase.common

import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke() = sessionRepository.getCurrentUser()
}