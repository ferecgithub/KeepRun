package com.ferechamitbeyli.domain.usecase.common.session_remote

import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class GetCurrentUserFromRemoteDBUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(identifier: String) = sessionRepository.getCurrentUserFromRemoteDB(identifier)
}