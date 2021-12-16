package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.presentation.uimodels.RunUIModel
import com.ferechamitbeyli.presentation.uimodels.mappers.RunToUIMapper
import com.ferechamitbeyli.presentation.utils.enums.RunSortType
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.usecases.RunUseCases
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunsViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    private val runUseCases: RunUseCases,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    var runSortType = RunSortType.DATE

    init {
        sortRuns(runSortType)
    }

    private var _runEventsChannel = MutableSharedFlow<EventState>()
    val runEventsChannel: SharedFlow<EventState> = _runEventsChannel

    private var _runs = MutableStateFlow<List<Run>>(mutableListOf())
    val runs: StateFlow<List<Run>> = _runs

    private val runsSortedByDate = flow {
        runUseCases.getAllRunsSortedByDateUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                emit(response.data)
            }
        }
    }

    private val runsSortedByRunTime = flow {
        runUseCases.getAllRunsSortedByTimeInMillisUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                emit(response.data)
            }
        }
    }

    private val runsSortedByCaloriesBurned = flow {
        runUseCases.getAllRunsSortedByCaloriesBurnedUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                emit(response.data)
            }
        }
    }

    private val runsSortedByDistance = flow {
        runUseCases.getAllRunsSortedByDistanceUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                emit(response.data)
            }
        }
    }

    private val runsSortedByAvgSpeed = flow {
        runUseCases.getAllRunsSortedByAverageSpeedUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                emit(response.data)
            }
        }
    }

    private val runsSortedByStepCount = flow {
        runUseCases.getAllRunsSortedByStepCountUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                emit(response.data)
            }
        }
    }

    fun removeRunFromSynced(run: RunUIModel) = ioScope.launch {
        val runToBeDeleted = RunToUIMapper.mapToDomainModel(run)
        runUseCases.removeRunUseCase.invoke(runToBeDeleted)
        runUseCases.removeMapImageFromRemoteDBUseCase.invoke(runToBeDeleted.timestamp.toString()).collect { deleteImgResponse ->
            when(deleteImgResponse) {
                is Resource.Error -> {
                    _runEventsChannel.emit(EventState.Error(deleteImgResponse.message.toString()))
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
                is Resource.Success -> {
                    runUseCases.removeRunFromRemoteDBUseCase.invoke(RunToUIMapper.mapToDomainModel(run)).collect { deleteResponse ->
                        when(deleteResponse) {
                            is Resource.Error -> {
                                _runEventsChannel.emit(EventState.Error(deleteResponse.message.toString()))
                            }
                            is Resource.Loading -> {
                                /** NO-OP **/
                            }
                            is Resource.Success -> {
                                _runEventsChannel.emit(EventState.Success(deleteResponse.data.toString()))
                            }
                        }
                    }
                }
            }
        }

    }

    fun getUserWeightFromRemoteDB() = flow<Double> {
        sessionUseCases.getUserWeightFromRemoteDBUseCase.invoke().collect { remoteDBResponse ->
            when (remoteDBResponse) {
                is Resource.Success -> {
                    remoteDBResponse.data?.let { weight -> emit(weight) }
                }
                is Resource.Error -> {
                    _runEventsChannel.emit(EventState.Error(remoteDBResponse.message.toString()))
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
            }
        }
    }

    fun getUserWeightFromCache() = flow<Double> {
        sessionUseCases.getUserWeightUseCase.invoke().collect {
            emit(it)
        }
    }

    fun sortRuns(sortType: RunSortType) = ioScope.launch {
        when (sortType) {
            RunSortType.DATE -> {
                runsSortedByDate.collectLatest { runsList ->
                    runsList?.let { _runs.value = it }
                }
            }
            RunSortType.RUN_TIME -> {
                runsSortedByRunTime.collectLatest { runsList ->
                    runsList?.let { _runs.value = it }
                }
            }
            RunSortType.CALORIES_BURNED -> {
                runsSortedByCaloriesBurned.collectLatest { runsList ->
                    runsList?.let { _runs.value = it }
                }
            }
            RunSortType.AVG_SPEED -> {
                runsSortedByAvgSpeed.collectLatest { runsList ->
                    runsList?.let { _runs.value = it }
                }
            }
            RunSortType.DISTANCE -> {
                runsSortedByDistance.collectLatest { runsList ->
                    runsList?.let { _runs.value = it }
                }
            }
            RunSortType.STEP_COUNT -> {
                runsSortedByStepCount.collectLatest { runsList ->
                    runsList?.let { _runs.value = it }
                }
            }
        }.also {
            this@RunsViewModel.runSortType = sortType
        }
    }

    fun getAllRunsFromRemoteDatabase() = flow<List<Run>> {
        runUseCases.getAllRunsFromRemoteDBUseCase.invoke().collectLatest { remoteDBResponse ->
            when (remoteDBResponse) {
                is Resource.Error -> {
                    _runEventsChannel.emit(EventState.Error(remoteDBResponse.message.toString()))
                }
                is Resource.Loading -> {
                    /** NO-OP **/
                }
                is Resource.Success -> {
                    remoteDBResponse.data?.let {
                        runUseCases.insertMultipleRunsUseCase.invoke(*it.toTypedArray())
                    }
                }
            }
        }
    }
}