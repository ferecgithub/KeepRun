package com.ferechamitbeyli.domain.usecase.run.run_local

import com.ferechamitbeyli.domain.repository.RunRepository
import javax.inject.Inject

class RemoveAllRunsUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.removeAllRunsFromDB()
}