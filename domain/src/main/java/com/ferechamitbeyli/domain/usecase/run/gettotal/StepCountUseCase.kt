package com.ferechamitbeyli.domain.usecase.run.gettotal

import com.ferechamitbeyli.domain.repository.RunRepository

class StepCountUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.getTotalStepCount()
}