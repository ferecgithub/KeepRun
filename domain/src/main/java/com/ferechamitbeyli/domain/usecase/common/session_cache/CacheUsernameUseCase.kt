package com.ferechamitbeyli.domain.usecase.common.session_cache

import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class CacheUsernameUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(username: String) = sessionRepository.cacheUsername(username)
}