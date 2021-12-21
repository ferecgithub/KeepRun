package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments

import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.uimodels.StatisticsUIModel
import com.ferechamitbeyli.presentation.utils.enums.DateType
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.helpers.StatisticsHelperFunctions.getTimestampOfStartAndEndDatesForMonth
import com.ferechamitbeyli.presentation.utils.helpers.StatisticsHelperFunctions.getTimestampOfStartAndEndDatesForWeek
import com.ferechamitbeyli.presentation.utils.helpers.StatisticsHelperFunctions.getTimestampOfToday
import com.ferechamitbeyli.presentation.utils.usecases.RunUseCases
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    private val runUseCases: RunUseCases,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    var dateType = DateType.TODAY
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    init {
        displayStatisticsByDate(dateType)
    }

    private var _statisticsFlow = MutableStateFlow<List<StatisticsUIModel>>(mutableListOf())
    val statisticsFlow: StateFlow<List<StatisticsUIModel>> = _statisticsFlow

    fun displayStatisticsByDate(dateType: DateType) = ioScope.launch {
        when (dateType) {
            DateType.TODAY -> {
                val dateRangeArray = getTimestampOfToday()

                val formattedDate = dateFormat.format(dateRangeArray[0])

                combine(
                    runUseCases.getTotalTimeInMillisBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalAverageSpeedInKMHBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalDistanceInMetersBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalCaloriesBurnedBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalStepCountBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    )
                ) { totalTimeResponse, totalAvgSpeedResponse, totalDistanceResponse, totalCaloriesResponse, totalStepsResponse ->
                    val statisticsUIModel = StatisticsUIModel(
                        dateTitle = "Today",
                        dateRange = formattedDate,
                        totalRunTime = totalTimeResponse.data.toString(),
                        totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                        totalRunDistance = totalDistanceResponse.data.toString(),
                        totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                        totalSteps = totalStepsResponse.data.toString()
                    )
                    _statisticsFlow.emit(listOf(statisticsUIModel))
                }
            }

            DateType.THIS_WEEK -> {
                val dateRangeArray = getTimestampOfStartAndEndDatesForWeek()

                val formattedStartDate = dateFormat.format(dateRangeArray[0])
                val formattedEndDate = dateFormat.format(dateRangeArray[1])

                combine(
                    runUseCases.getTotalTimeInMillisBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalAverageSpeedInKMHBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalDistanceInMetersBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalCaloriesBurnedBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalStepCountBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    )
                ) { totalTimeResponse, totalAvgSpeedResponse, totalDistanceResponse, totalCaloriesResponse, totalStepsResponse ->
                    val statisticsUIModel = StatisticsUIModel(
                        dateTitle = "This week",
                        dateRange = "$formattedStartDate - $formattedEndDate",
                        totalRunTime = totalTimeResponse.data.toString(),
                        totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                        totalRunDistance = totalDistanceResponse.data.toString(),
                        totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                        totalSteps = totalStepsResponse.data.toString()
                    )
                    _statisticsFlow.emit(listOf(statisticsUIModel))
                }


            }

            DateType.THIS_MONTH -> {
                val dateRangeArray = getTimestampOfStartAndEndDatesForMonth()

                val dateOfFirstWeekRangeArray = getTimestampOfStartAndEndDatesForWeek(0)
                val dateOfSecondWeekRangeArray = getTimestampOfStartAndEndDatesForWeek(-1)
                val dateOfThirdWeekRangeArray = getTimestampOfStartAndEndDatesForWeek(-2)
                val dateOfFourthWeekRangeArray = getTimestampOfStartAndEndDatesForWeek(-3)

                val formattedStartOfMonthDate = dateFormat.format(dateRangeArray[0])
                val formattedEndOfMonthDate = dateFormat.format(dateRangeArray[1])

                val formattedStartOfFirstWeekDate = dateFormat.format(dateOfFirstWeekRangeArray[0])
                val formattedEndOfFirstWeekDate = dateFormat.format(dateOfFirstWeekRangeArray[1])

                val formattedStartOfSecondWeekDate =
                    dateFormat.format(dateOfSecondWeekRangeArray[0])
                val formattedEndOfSecondWeekDate = dateFormat.format(dateOfSecondWeekRangeArray[1])

                val formattedStartOfThirdWeekDate = dateFormat.format(dateOfThirdWeekRangeArray[0])
                val formattedEndOfThirdWeekDate = dateFormat.format(dateOfThirdWeekRangeArray[1])

                val formattedStartOfFourthWeekDate =
                    dateFormat.format(dateOfFourthWeekRangeArray[0])
                val formattedEndOfFourthWeekDate = dateFormat.format(dateOfFourthWeekRangeArray[1])

                combine(
                    runUseCases.getTotalTimeInMillisBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalAverageSpeedInKMHBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalDistanceInMetersBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalCaloriesBurnedBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    ),
                    runUseCases.getTotalStepCountBetweenUseCase.invoke(
                        dateRangeArray[0],
                        dateRangeArray[1]
                    )
                ) { totalTimeResponse, totalAvgSpeedResponse, totalDistanceResponse, totalCaloriesResponse, totalStepsResponse ->

                    val statisticsThisMonthUIModel = StatisticsUIModel(
                        dateTitle = "This month",
                        dateRange = "$formattedStartOfMonthDate - $formattedEndOfMonthDate",
                        totalRunTime = totalTimeResponse.data.toString(),
                        totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                        totalRunDistance = totalDistanceResponse.data.toString(),
                        totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                        totalSteps = totalStepsResponse.data.toString()
                    )

                    val statisticsFirstWeekUIModel = StatisticsUIModel(
                        dateTitle = "First week",
                        dateRange = "$formattedStartOfFirstWeekDate - $formattedEndOfFirstWeekDate",
                        totalRunTime = totalTimeResponse.data.toString(),
                        totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                        totalRunDistance = totalDistanceResponse.data.toString(),
                        totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                        totalSteps = totalStepsResponse.data.toString()
                    )








                    _statisticsFlow.emit(listOf(statisticsThisMonthUIModel, ))
                }

            }
        }.also {
            this@StatisticsViewModel.dateType = dateType
        }
    }

}