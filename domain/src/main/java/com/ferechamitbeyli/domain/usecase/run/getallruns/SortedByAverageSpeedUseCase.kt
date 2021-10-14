package com.ferechamitbeyli.domain.usecase.run.getallruns

import com.ferechamitbeyli.domain.repository.RunRepository

class SortedByAverageSpeedUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.getAllRunsSortedByAverageSpeed()
}