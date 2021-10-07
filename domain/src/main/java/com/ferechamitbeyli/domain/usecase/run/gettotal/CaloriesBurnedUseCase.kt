package com.ferechamitbeyli.core.usecase.run.gettotal

import com.ferechamitbeyli.core.repository.RunRepository

class CaloriesBurnedUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.getTotalCaloriesBurned()
}