package com.ferechamitbeyli.domain.usecase.run

import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.RunRepository

class RemoveRunUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(run: Run) = runRepository.remove(run)
}