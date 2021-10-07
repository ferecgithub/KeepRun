package com.ferechamitbeyli.core.usecase.run.getallruns

import com.ferechamitbeyli.core.repository.RunRepository

class SortedByAverageSpeedUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.getAllRunsSortedByAverageSpeed()
}