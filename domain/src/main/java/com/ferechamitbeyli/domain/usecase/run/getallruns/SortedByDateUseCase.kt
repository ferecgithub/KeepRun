package com.ferechamitbeyli.domain.usecase.run.getallruns

import com.ferechamitbeyli.domain.repository.RunRepository

class SortedByDateUseCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.getAllRunsSortedByDate()
}