package com.ferechamitbeyli.domain.usecase.run.gettotal

import com.ferechamitbeyli.domain.repository.RunRepository

class AverageSpeedInKMHUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.getTotalAverageSpeedInKMH()
}