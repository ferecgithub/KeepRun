package com.ferechamitbeyli.domain.usecase.common

import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class UpdateUsernameToRemoteDBUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(username: String) = sessionRepository.updateUsernameToRemoteDB(username)
}