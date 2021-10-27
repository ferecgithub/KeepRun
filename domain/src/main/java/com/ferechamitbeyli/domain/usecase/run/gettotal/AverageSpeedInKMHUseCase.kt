package com.ferechamitbeyli.domain.usecase.run.gettotal

import com.ferechamitbeyli.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AverageSpeedInKMHUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(): Flow<Float> = runRepository.getTotalAverageSpeedInKMH()
}