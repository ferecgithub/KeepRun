package com.ferechamitbeyli.domain.usecase.run.run_remote

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertRunToRemoteDBUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(run: Run, imageUrl: String): Flow<Resource<String>> =
        runRepository.insertRunToRemoteDB(run, imageUrl)
}
