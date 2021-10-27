package com.ferechamitbeyli.domain.usecase.run.gettotal

import com.ferechamitbeyli.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CaloriesBurnedUseCase @Inject constructor(
    private val runRepository: RunRepository
) {
    suspend operator fun invoke(): Flow<Int> = runRepository.getTotalCaloriesBurned()
}