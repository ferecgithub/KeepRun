package com.ferechamitbeyli.domain.usecase.run.run_remote

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteMapImageFromRemoteDBUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(timestamp: String): Flow<Resource<String>> =
        runRepository.removeMapImageFromRemoteDB(timestamp)
}