package com.ferechamitbeyli.domain.repository

import javax.inject.Inject

class GetUsernameFromRemoteDB @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke() = sessionRepository.getUsernameFromRemoteDB()
}