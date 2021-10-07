package com.ferechamitbeyli.core.usecase.run

import com.ferechamitbeyli.core.data.Run
import com.ferechamitbeyli.core.repository.RunRepository

class RemoveRunUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(run: Run) = runRepository.remove(run)
}