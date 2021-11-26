package com.ferechamitbeyli.domain.usecase.common.session_cache

import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class CacheActivityRecognitionPermissionStateUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(hasPermission: Boolean) = sessionRepository.cacheActivityRecognitionPermissionState(hasPermission)
}