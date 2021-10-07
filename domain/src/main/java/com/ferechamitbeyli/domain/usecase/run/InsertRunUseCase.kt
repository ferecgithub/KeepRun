package com.ferechamitbeyli.core.usecase.run

import com.ferechamitbeyli.core.data.Run
import com.ferechamitbeyli.core.repository.RunRepository

class InsertRunUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(run: Run) = runRepository.insert(run)
}