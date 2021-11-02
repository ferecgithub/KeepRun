package com.ferechamitbeyli.domain.usecase.common

import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class CacheInitialSetupStateUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(hasInitialSetupDone: Boolean) = sessionRepository.cacheInitialSetupState(hasInitialSetupDone)
}