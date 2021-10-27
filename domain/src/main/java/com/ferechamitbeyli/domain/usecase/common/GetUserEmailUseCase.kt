package com.ferechamitbeyli.domain.usecase.common

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserEmailUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): Flow<Resource<String>> = sessionRepository.getUserEmail()
}