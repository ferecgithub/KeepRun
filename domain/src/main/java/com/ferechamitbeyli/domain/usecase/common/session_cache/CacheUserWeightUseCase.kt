package com.ferechamitbeyli.domain.usecase.common.session_cache

import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class CacheUserWeightUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(weight: Double) = sessionRepository.cacheUserWeight(weight)
}