package com.ferechamitbeyli.core.usecase.run.getallruns

import com.ferechamitbeyli.core.repository.RunRepository

class SortedByTimeInMillisUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.getAllRunsSortedByTimeInMillis()
}