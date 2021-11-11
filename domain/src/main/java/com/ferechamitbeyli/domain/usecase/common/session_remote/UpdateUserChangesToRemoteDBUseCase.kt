package com.ferechamitbeyli.domain.usecase.common.session_remote

import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.SessionRepository
import javax.inject.Inject

class UpdateUserChangesToRemoteDBUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(user: User) = sessionRepository.updateUserChangesToRemoteDB(user)
}