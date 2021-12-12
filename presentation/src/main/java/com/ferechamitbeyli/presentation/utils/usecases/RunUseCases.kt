package com.ferechamitbeyli.presentation.utils.usecases

import com.ferechamitbeyli.domain.usecase.run.run_local.InsertMultipleRunsUseCase
import com.ferechamitbeyli.domain.usecase.run.run_local.InsertRunUseCase
import com.ferechamitbeyli.domain.usecase.run.run_local.RemoveAllRunsUseCase
import com.ferechamitbeyli.domain.usecase.run.run_local.RemoveRunUseCase
import com.ferechamitbeyli.domain.usecase.run.run_local.getallruns.*
import com.ferechamitbeyli.domain.usecase.run.run_local.gettotal.*
import com.ferechamitbeyli.domain.usecase.run.run_remote.*
import javax.inject.Inject

data class RunUseCases @Inject constructor(
    val getAllRunsSortedByAverageSpeedUseCase: SortedByAverageSpeedUseCase,
    val getAllRunsSortedByCaloriesBurnedUseCase: SortedByCaloriesBurnedUseCase,
    val getAllRunsSortedByDateUseCase: SortedByDateUseCase,
    val getAllRunsSortedByDistanceUseCase: SortedByDistanceUseCase,
    val getAllRunsSortedByTimeInMillisUseCase: SortedByTimeInMillisUseCase,
    val getAllRunsSortedByStepCountUseCase: SortedByStepCountUseCase,

    val getTotalAverageSpeedUseCase: AverageSpeedInKMHUseCase,
    val getTotalCaloriesBurnedUseCase: CaloriesBurnedUseCase,
    val getTotalDistanceInMetersUseCase: DistanceInMetersUseCase,
    val getTotalStepCountUseCase: StepCountUseCase,
    val getTotalTimeInMillisUseCase: TimeInMillisUseCase,

    val insertMultipleRunsUseCase: InsertMultipleRunsUseCase,
    val insertRunUseCase: InsertRunUseCase,
    val removeAllRunsUseCase: RemoveAllRunsUseCase,
    val removeRunUseCase: RemoveRunUseCase,

    val getAllRunsFromRemoteDBUseCase: GetAllRunsFromRemoteDBUseCase,
    val insertMapImageToRemoteDBUseCase: InsertMapImageToRemoteDBUseCase,
    val insertRunToRemoteDBUseCase: InsertRunToRemoteDBUseCase,
    val removeMapImageFromRemoteDBUseCase: RemoveMapImageFromRemoteDBUseCase,
    val removeRunFromRemoteDBUseCase: RemoveRunFromRemoteDBUseCase
)
