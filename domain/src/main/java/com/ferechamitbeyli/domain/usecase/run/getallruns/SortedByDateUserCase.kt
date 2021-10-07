package com.ferechamitbeyli.core.usecase.run.getallruns

import com.ferechamitbeyli.core.data.Run
import com.ferechamitbeyli.core.repository.RunRepository

class SortedByDateUserCase(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke() = runRepository.getAllRunsSortedByDate()
}