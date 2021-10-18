package com.ferechamitbeyli.keeprun.framework.model.usecases

import com.ferechamitbeyli.domain.usecase.run.InsertRunUseCase
import com.ferechamitbeyli.domain.usecase.run.RemoveRunUseCase
import com.ferechamitbeyli.domain.usecase.run.getallruns.*
import com.ferechamitbeyli.domain.usecase.run.gettotal.AverageSpeedInKMHUseCase
import com.ferechamitbeyli.domain.usecase.run.gettotal.DistanceInMetersUseCase
import com.ferechamitbeyli.domain.usecase.run.gettotal.StepCountUseCase

data class HomeUseCases(
    val getAllRunsSortedByAverageSpeedUseCase: SortedByAverageSpeedUseCase,
    val getAllRunsSortedByCaloriesBurnedUseCase: SortedByCaloriesBurnedUseCase,
    val getAllRunsSortedByDateUseCase: SortedByDateUseCase,
    val getAllRunsSortedByDistanceUseCase: SortedByDistanceUseCase,
    val getAllRunsSortedByTimeInMillisUseCase: SortedByTimeInMillisUseCase,

    val getTotalAverageSpeedInKMHUseCase: AverageSpeedInKMHUseCase,
    val getTotalCaloriesBurnedUseCase: SortedByCaloriesBurnedUseCase,
    val getTotalDistanceInMetersUseCase: DistanceInMetersUseCase,
    val getTotalStepCountUseCase: StepCountUseCase,
    val getTotalTimeInMillisUseCase: SortedByTimeInMillisUseCase,

    val insertRunUseCase: InsertRunUseCase,
    val removeRunUseCase: RemoveRunUseCase
)