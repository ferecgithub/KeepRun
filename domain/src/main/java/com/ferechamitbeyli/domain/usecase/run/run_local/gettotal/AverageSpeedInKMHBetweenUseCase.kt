package com.ferechamitbeyli.domain.usecase.run.run_local.gettotal

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AverageSpeedInKMHBetweenUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(startDate: Long, endDate:Long): Flow<Resource<Double?>> =
        runRepository.getTotalAverageSpeedInKMHBetween(startDate, endDate)
}