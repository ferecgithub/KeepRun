package com.ferechamitbeyli.domain.usecase.run.getallruns

import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SortedByTimeInMillisUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(): Flow<List<Run>> = runRepository.getAllRunsSortedByTimeInMillis()
}