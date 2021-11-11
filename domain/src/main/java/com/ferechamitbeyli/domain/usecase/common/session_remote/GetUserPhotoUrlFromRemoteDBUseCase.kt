package com.ferechamitbeyli.domain.usecase.common.session_remote

import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class GetUserPhotoUrlFromRemoteDBUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke() = sessionRepository.getUserPhotoUrlFromRemoteDB()
}