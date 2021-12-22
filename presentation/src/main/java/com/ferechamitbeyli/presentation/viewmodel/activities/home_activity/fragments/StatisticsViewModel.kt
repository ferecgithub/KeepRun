package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments

import androidx.lifecycle.SavedStateHandle
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.uimodels.StatisticsUIModel
import com.ferechamitbeyli.presentation.utils.enums.DateType
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.helpers.StatisticsHelperFunctions.getTimestampOfStartAndEndDatesForMonth
import com.ferechamitbeyli.presentation.utils.helpers.StatisticsHelperFunctions.getTimestampOfStartAndEndDatesForWeek
import com.ferechamitbeyli.presentation.utils.helpers.StatisticsHelperFunctions.getTimestampOfToday
import com.ferechamitbeyli.presentation.utils.usecases.RunUseCases
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val runUseCases: RunUseCases,
    private val savedStateHandle: SavedStateHandle,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    var dateType = savedStateHandle["dateType"] ?: DateType.TODAY

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    init {
        displayStatisticsByDate(dateType)
    }

    private var _statisticsFlow = MutableStateFlow<List<StatisticsUIModel>>(mutableListOf())
    val statisticsFlow: StateFlow<List<StatisticsUIModel>> = _statisticsFlow

    private fun getStatisticsOfWholeOfTheMonth() = flow<StatisticsUIModel> {
        val dateRangeArray = getTimestampOfStartAndEndDatesForMonth()

        val formattedStartOfMonthDate = dateFormat.format(dateRangeArray[0])
        val formattedEndOfMonthDate = dateFormat.format(dateRangeArray[1])


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
            var statisticsUIModel: StatisticsUIModel? = null
            if (totalTimeResponse.data != null && totalAvgSpeedResponse.data != null && totalDistanceResponse.data != null &&
                totalCaloriesResponse.data != null && totalStepsResponse.data != null
            ) {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "This month",
                    dateRange = "$formattedStartOfMonthDate - $formattedEndOfMonthDate",
                    totalRunTime = totalTimeResponse.data.toString(),
                    totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                    totalRunDistance = totalDistanceResponse.data.toString(),
                    totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                    totalSteps = totalStepsResponse.data.toString()
                )
            } else {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "This month",
                    dateRange = "$formattedStartOfMonthDate - $formattedEndOfMonthDate",
                    totalRunTime = "0",
                    totalAvgRunSpeed = "0",
                    totalRunDistance = "0",
                    totalCaloriesBurned = "0",
                    totalSteps = "0"
                )
            }
            statisticsUIModel
        }.collect {
            emit(it)
        }

    }

    private fun getStatisticsOfFirstWeekOfTheMonth() = flow<StatisticsUIModel> {
        val dateOfFirstWeekRangeArray = getTimestampOfStartAndEndDatesForWeek(-3)

        val formattedStartOfFirstWeekDate = dateFormat.format(dateOfFirstWeekRangeArray[0])
        val formattedEndOfFirstWeekDate = dateFormat.format(dateOfFirstWeekRangeArray[1])

        combine(
            runUseCases.getTotalTimeInMillisBetweenUseCase.invoke(
                dateOfFirstWeekRangeArray[0],
                dateOfFirstWeekRangeArray[1]
            ),
            runUseCases.getTotalAverageSpeedInKMHBetweenUseCase.invoke(
                dateOfFirstWeekRangeArray[0],
                dateOfFirstWeekRangeArray[1]
            ),
            runUseCases.getTotalDistanceInMetersBetweenUseCase.invoke(
                dateOfFirstWeekRangeArray[0],
                dateOfFirstWeekRangeArray[1]
            ),
            runUseCases.getTotalCaloriesBurnedBetweenUseCase.invoke(
                dateOfFirstWeekRangeArray[0],
                dateOfFirstWeekRangeArray[1]
            ),
            runUseCases.getTotalStepCountBetweenUseCase.invoke(
                dateOfFirstWeekRangeArray[0],
                dateOfFirstWeekRangeArray[1]
            )
        ) { totalTimeResponse, totalAvgSpeedResponse, totalDistanceResponse, totalCaloriesResponse, totalStepsResponse ->
            var statisticsUIModel: StatisticsUIModel? = null
            if (totalTimeResponse.data != null && totalAvgSpeedResponse.data != null && totalDistanceResponse.data != null &&
                totalCaloriesResponse.data != null && totalStepsResponse.data != null
            ) {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "First week",
                    dateRange = "$formattedStartOfFirstWeekDate - $formattedEndOfFirstWeekDate",
                    totalRunTime = totalTimeResponse.data.toString(),
                    totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                    totalRunDistance = totalDistanceResponse.data.toString(),
                    totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                    totalSteps = totalStepsResponse.data.toString()
                )
            } else {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "First week",
                    dateRange = "$formattedStartOfFirstWeekDate - $formattedEndOfFirstWeekDate",
                    totalRunTime = "0",
                    totalAvgRunSpeed = "0",
                    totalRunDistance = "0",
                    totalCaloriesBurned = "0",
                    totalSteps = "0"
                )
            }
            statisticsUIModel
        }.collect {
            emit(it)
        }


    }

    private fun getStatisticsOfSecondWeekOfTheMonth() = flow<StatisticsUIModel> {
        val dateOfSecondWeekRangeArray = getTimestampOfStartAndEndDatesForWeek(-2)

        val formattedStartOfSecondWeekDate =
            dateFormat.format(dateOfSecondWeekRangeArray[0])
        val formattedEndOfSecondWeekDate = dateFormat.format(dateOfSecondWeekRangeArray[1])

        combine(
            runUseCases.getTotalTimeInMillisBetweenUseCase.invoke(
                dateOfSecondWeekRangeArray[0],
                dateOfSecondWeekRangeArray[1]
            ),
            runUseCases.getTotalAverageSpeedInKMHBetweenUseCase.invoke(
                dateOfSecondWeekRangeArray[0],
                dateOfSecondWeekRangeArray[1]
            ),
            runUseCases.getTotalDistanceInMetersBetweenUseCase.invoke(
                dateOfSecondWeekRangeArray[0],
                dateOfSecondWeekRangeArray[1]
            ),
            runUseCases.getTotalCaloriesBurnedBetweenUseCase.invoke(
                dateOfSecondWeekRangeArray[0],
                dateOfSecondWeekRangeArray[1]
            ),
            runUseCases.getTotalStepCountBetweenUseCase.invoke(
                dateOfSecondWeekRangeArray[0],
                dateOfSecondWeekRangeArray[1]
            )
        ) { totalTimeResponse, totalAvgSpeedResponse, totalDistanceResponse, totalCaloriesResponse, totalStepsResponse ->
            var statisticsUIModel: StatisticsUIModel? = null
            if (totalTimeResponse.data != null && totalAvgSpeedResponse.data != null && totalDistanceResponse.data != null &&
                totalCaloriesResponse.data != null && totalStepsResponse.data != null
            ) {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "Second week",
                    dateRange = "$formattedStartOfSecondWeekDate - $formattedEndOfSecondWeekDate",
                    totalRunTime = totalTimeResponse.data.toString(),
                    totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                    totalRunDistance = totalDistanceResponse.data.toString(),
                    totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                    totalSteps = totalStepsResponse.data.toString()
                )
            } else {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "Second week",
                    dateRange = "$formattedStartOfSecondWeekDate - $formattedEndOfSecondWeekDate",
                    totalRunTime = "0",
                    totalAvgRunSpeed = "0",
                    totalRunDistance = "0",
                    totalCaloriesBurned = "0",
                    totalSteps = "0"
                )
            }
            statisticsUIModel
        }.collect {
            emit(it)
        }

    }

    private fun getStatisticsOfThirdWeekOfTheMonth() = flow<StatisticsUIModel> {
        val dateOfThirdWeekRangeArray = getTimestampOfStartAndEndDatesForWeek(-1)

        val formattedStartOfThirdWeekDate = dateFormat.format(dateOfThirdWeekRangeArray[0])
        val formattedEndOfThirdWeekDate = dateFormat.format(dateOfThirdWeekRangeArray[1])

        combine(
            runUseCases.getTotalTimeInMillisBetweenUseCase.invoke(
                dateOfThirdWeekRangeArray[0],
                dateOfThirdWeekRangeArray[1]
            ),
            runUseCases.getTotalAverageSpeedInKMHBetweenUseCase.invoke(
                dateOfThirdWeekRangeArray[0],
                dateOfThirdWeekRangeArray[1]
            ),
            runUseCases.getTotalDistanceInMetersBetweenUseCase.invoke(
                dateOfThirdWeekRangeArray[0],
                dateOfThirdWeekRangeArray[1]
            ),
            runUseCases.getTotalCaloriesBurnedBetweenUseCase.invoke(
                dateOfThirdWeekRangeArray[0],
                dateOfThirdWeekRangeArray[1]
            ),
            runUseCases.getTotalStepCountBetweenUseCase.invoke(
                dateOfThirdWeekRangeArray[0],
                dateOfThirdWeekRangeArray[1]
            )
        ) { totalTimeResponse, totalAvgSpeedResponse, totalDistanceResponse, totalCaloriesResponse, totalStepsResponse ->
            var statisticsUIModel: StatisticsUIModel? = null
            if (totalTimeResponse.data != null && totalAvgSpeedResponse.data != null && totalDistanceResponse.data != null &&
                totalCaloriesResponse.data != null && totalStepsResponse.data != null
            ) {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "Third week",
                    dateRange = "$formattedStartOfThirdWeekDate - $formattedEndOfThirdWeekDate",
                    totalRunTime = totalTimeResponse.data.toString(),
                    totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                    totalRunDistance = totalDistanceResponse.data.toString(),
                    totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                    totalSteps = totalStepsResponse.data.toString()
                )
            } else {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "Third week",
                    dateRange = "$formattedStartOfThirdWeekDate - $formattedEndOfThirdWeekDate",
                    totalRunTime = "0",
                    totalAvgRunSpeed = "0",
                    totalRunDistance = "0",
                    totalCaloriesBurned = "0",
                    totalSteps = "0"
                )
            }
            statisticsUIModel
        }.collect {
            emit(it)
        }

    }

    private fun getStatisticsOfFourthWeekOfTheMonth() = flow<StatisticsUIModel> {
        val dateOfFourthWeekRangeArray = getTimestampOfStartAndEndDatesForWeek(0)

        val formattedStartOfFourthWeekDate =
            dateFormat.format(dateOfFourthWeekRangeArray[0])
        val formattedEndOfFourthWeekDate = dateFormat.format(dateOfFourthWeekRangeArray[1])

        combine(
            runUseCases.getTotalTimeInMillisBetweenUseCase.invoke(
                dateOfFourthWeekRangeArray[0],
                dateOfFourthWeekRangeArray[1]
            ),
            runUseCases.getTotalAverageSpeedInKMHBetweenUseCase.invoke(
                dateOfFourthWeekRangeArray[0],
                dateOfFourthWeekRangeArray[1]
            ),
            runUseCases.getTotalDistanceInMetersBetweenUseCase.invoke(
                dateOfFourthWeekRangeArray[0],
                dateOfFourthWeekRangeArray[1]
            ),
            runUseCases.getTotalCaloriesBurnedBetweenUseCase.invoke(
                dateOfFourthWeekRangeArray[0],
                dateOfFourthWeekRangeArray[1]
            ),
            runUseCases.getTotalStepCountBetweenUseCase.invoke(
                dateOfFourthWeekRangeArray[0],
                dateOfFourthWeekRangeArray[1]
            )
        ) { totalTimeResponse, totalAvgSpeedResponse, totalDistanceResponse, totalCaloriesResponse, totalStepsResponse ->
            var statisticsUIModel: StatisticsUIModel? = null
            if (totalTimeResponse.data != null && totalAvgSpeedResponse.data != null && totalDistanceResponse.data != null &&
                totalCaloriesResponse.data != null && totalStepsResponse.data != null
            ) {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "Fourth week",
                    dateRange = "$formattedStartOfFourthWeekDate - $formattedEndOfFourthWeekDate",
                    totalRunTime = totalTimeResponse.data.toString(),
                    totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                    totalRunDistance = totalDistanceResponse.data.toString(),
                    totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                    totalSteps = totalStepsResponse.data.toString()
                )
            } else {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "Fourth week",
                    dateRange = "$formattedStartOfFourthWeekDate - $formattedEndOfFourthWeekDate",
                    totalRunTime = "0",
                    totalAvgRunSpeed = "0",
                    totalRunDistance = "0",
                    totalCaloriesBurned = "0",
                    totalSteps = "0"
                )
            }
            statisticsUIModel
        }.collect {
            emit(it)
        }

    }

    private fun getStatisticsOfThisWeek() = flow<StatisticsUIModel> {
        val dateOfTheWeekRangeArray = getTimestampOfStartAndEndDatesForWeek()

        val formattedStartOfTheWeekDate =
            dateFormat.format(dateOfTheWeekRangeArray[0])
        val formattedEndOfTheWeekDate = dateFormat.format(dateOfTheWeekRangeArray[1])

        combine(
            runUseCases.getTotalTimeInMillisBetweenUseCase.invoke(
                dateOfTheWeekRangeArray[0],
                dateOfTheWeekRangeArray[1]
            ),
            runUseCases.getTotalAverageSpeedInKMHBetweenUseCase.invoke(
                dateOfTheWeekRangeArray[0],
                dateOfTheWeekRangeArray[1]
            ),
            runUseCases.getTotalDistanceInMetersBetweenUseCase.invoke(
                dateOfTheWeekRangeArray[0],
                dateOfTheWeekRangeArray[1]
            ),
            runUseCases.getTotalCaloriesBurnedBetweenUseCase.invoke(
                dateOfTheWeekRangeArray[0],
                dateOfTheWeekRangeArray[1]
            ),
            runUseCases.getTotalStepCountBetweenUseCase.invoke(
                dateOfTheWeekRangeArray[0],
                dateOfTheWeekRangeArray[1]
            )
        ) { totalTimeResponse, totalAvgSpeedResponse, totalDistanceResponse, totalCaloriesResponse, totalStepsResponse ->
            var statisticsUIModel: StatisticsUIModel? = null
            if (totalTimeResponse.data != null && totalAvgSpeedResponse.data != null && totalDistanceResponse.data != null &&
                totalCaloriesResponse.data != null && totalStepsResponse.data != null
            ) {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "This week",
                    dateRange = "$formattedStartOfTheWeekDate - $formattedEndOfTheWeekDate",
                    totalRunTime = totalTimeResponse.data.toString(),
                    totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                    totalRunDistance = totalDistanceResponse.data.toString(),
                    totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                    totalSteps = totalStepsResponse.data.toString()
                )
            } else {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "This week",
                    dateRange = "$formattedStartOfTheWeekDate - $formattedEndOfTheWeekDate",
                    totalRunTime = "0",
                    totalAvgRunSpeed = "0",
                    totalRunDistance = "0",
                    totalCaloriesBurned = "0",
                    totalSteps = "0"
                )
            }
            listOf(statisticsUIModel)
        }.collect {
            _statisticsFlow.emit(it)
        }

    }

    private fun getStatisticsOfToday() = flow<StatisticsUIModel> {
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

            var statisticsUIModel: StatisticsUIModel? = null
            if (totalTimeResponse.data != null && totalAvgSpeedResponse.data != null && totalDistanceResponse.data != null &&
                totalCaloriesResponse.data != null && totalStepsResponse.data != null
            ) {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "Today",
                    dateRange = formattedDate,
                    totalRunTime = totalTimeResponse.data.toString(),
                    totalAvgRunSpeed = totalAvgSpeedResponse.data.toString(),
                    totalRunDistance = totalDistanceResponse.data.toString(),
                    totalCaloriesBurned = totalCaloriesResponse.data.toString(),
                    totalSteps = totalStepsResponse.data.toString()
                )
            } else {
                statisticsUIModel = StatisticsUIModel(
                    dateTitle = "Today",
                    dateRange = formattedDate,
                    totalRunTime = "0",
                    totalAvgRunSpeed = "0",
                    totalRunDistance = "0",
                    totalCaloriesBurned = "0",
                    totalSteps = "0"
                )
            }
            listOf(statisticsUIModel)
        }.collect {
            _statisticsFlow.emit(it)
        }

    }

    fun displayStatisticsByDate(dateType: DateType) = ioScope.launch {
        when (dateType) {
            DateType.TODAY -> {
                getStatisticsOfToday().collect()
            }

            DateType.THIS_WEEK -> {
                getStatisticsOfThisWeek().collect()
            }

            DateType.THIS_MONTH -> {

                combine(
                    getStatisticsOfWholeOfTheMonth(),
                    getStatisticsOfFirstWeekOfTheMonth(),
                    getStatisticsOfSecondWeekOfTheMonth(),
                    getStatisticsOfThirdWeekOfTheMonth(),
                    getStatisticsOfFourthWeekOfTheMonth()
                ) { month, firstWeek, secondWeek, thirdWeek, fourthWeek ->

                    listOf(month, firstWeek, secondWeek, thirdWeek, fourthWeek)
                }.collect {
                    _statisticsFlow.emit(it)
                }

            }

        }.also {
            this@StatisticsViewModel.dateType = dateType
            savedStateHandle["dateType"] = dateType
        }
    }

}