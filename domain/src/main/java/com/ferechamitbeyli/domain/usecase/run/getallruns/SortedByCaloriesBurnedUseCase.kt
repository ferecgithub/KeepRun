package com.ferechamitbeyli.domain.usecase.run.getallruns

import com.ferechamitbeyli.domain.repository.RunRepository

class SortedByCaloriesBurnedUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.getAllRunsSortedByCaloriesBurned()
}