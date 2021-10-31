package com.ferechamitbeyli.domain.usecase.common

import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class CacheUserAccountUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        userUid: String,
        username: String,
        userEmail: String,
        userNotificationEnabled: Boolean,
        userPhotoUrl: String
    ) = sessionRepository.cacheUserAccount(
        userUid, username, userEmail, userNotificationEnabled, userPhotoUrl
    )
}