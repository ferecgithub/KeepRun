package com.ferechamitbeyli.domain.usecase.run.run_local.getallruns

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SortedByTimeInMillisUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Run>>> = runRepository.getAllRunsSortedByTimeInMillis()
}