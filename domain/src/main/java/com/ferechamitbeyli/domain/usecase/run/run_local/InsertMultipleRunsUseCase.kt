package com.ferechamitbeyli.domain.usecase.run.run_local

import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.RunRepository
import javax.inject.Inject

class InsertMultipleRunsUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(vararg run: Run) = runRepository.insertMultipleRunsToDB(*run)
}