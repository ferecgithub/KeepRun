package com.ferechamitbeyli.domain.usecase.common.session_cache

import com.ferechamitbeyli.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserWeightUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): Flow<Double> = sessionRepository.getUserWeight()
}