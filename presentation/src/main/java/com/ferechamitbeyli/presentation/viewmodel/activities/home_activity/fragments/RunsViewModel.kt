package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments

import androidx.lifecycle.SavedStateHandle
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
    private val savedStateHandle: SavedStateHandle,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    var runSortType = savedStateHandle["runSortType"] ?: RunSortType.DATE

    init {
        sortRuns(runSortType)
    }

    private var _runEventsChannel = MutableSharedFlow<EventState>()
    val runEventsChannel: SharedFlow<EventState> = _runEventsChannel

    private var _runsFlow = MutableStateFlow<List<Run>>(mutableListOf())
    val runsFlow: StateFlow<List<Run>> = _runsFlow

    private fun runsSortedByDate() = ioScope.launch {
        runUseCases.getAllRunsSortedByDateUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                response.data?.let {
                    _runEventsChannel.emit(EventState.Success())
                    _runsFlow.emit(it)
                }
            }
        }
    }

    private fun runsSortedByRunTime() = ioScope.launch {
        runUseCases.getAllRunsSortedByTimeInMillisUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                response.data?.let {
                    _runEventsChannel.emit(EventState.Success())
                    _runsFlow.emit(it)
                }
            }
        }
    }

    private fun runsSortedByCaloriesBurned() = ioScope.launch {
        runUseCases.getAllRunsSortedByCaloriesBurnedUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                response.data?.let {
                    _runEventsChannel.emit(EventState.Success())
                    _runsFlow.emit(it)
                }
            }
        }
    }

    private fun runsSortedByDistance() = ioScope.launch {
        runUseCases.getAllRunsSortedByDistanceUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                response.data?.let {
                    _runEventsChannel.emit(EventState.Success())
                    _runsFlow.emit(it)
                }
            }
        }
    }

    private fun runsSortedByAvgSpeed() = ioScope.launch {
        runUseCases.getAllRunsSortedByAverageSpeedUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                response.data?.let { _runsFlow.emit(it) }
            }
        }
    }

    private fun runsSortedByStepCount() = ioScope.launch {
        runUseCases.getAllRunsSortedByStepCountUseCase.invoke().collect { response ->
            if (response is Resource.Success) {
                response.data?.let {
                    _runEventsChannel.emit(EventState.Success())
                    _runsFlow.emit(it)
                }
            }
        }
    }

    fun removeRunFromSynced(run: RunUIModel) = ioScope.launch {
        val runToBeDeleted = RunToUIMapper.mapToDomainModel(run)
        runUseCases.removeRunUseCase.invoke(runToBeDeleted)
        runUseCases.removeMapImageFromRemoteDBUseCase.invoke(runToBeDeleted.timestamp.toString())
            .collect { deleteImgResponse ->
                when (deleteImgResponse) {
                    is Resource.Error -> {
                        _runEventsChannel.emit(EventState.Error(deleteImgResponse.message.toString()))
                    }
                    is Resource.Loading -> {
                        _runEventsChannel.emit(EventState.Loading())
                    }
                    is Resource.Success -> {
                        runUseCases.removeRunFromRemoteDBUseCase.invoke(
                            RunToUIMapper.mapToDomainModel(
                                run
                            )
                        ).collect { deleteResponse ->
                            when (deleteResponse) {
                                is Resource.Error -> {
                                    _runEventsChannel.emit(EventState.Error(deleteResponse.message.toString()))
                                }
                                is Resource.Loading -> {
                                    _runEventsChannel.emit(EventState.Loading())
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
                is Resource.Error -> {
                    _runEventsChannel.emit(EventState.Error(remoteDBResponse.message.toString()))
                }
                is Resource.Loading -> {
                    _runEventsChannel.emit(EventState.Loading())
                }
                is Resource.Success -> {
                    remoteDBResponse.data?.let { weight -> emit(weight) }
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
                runsSortedByDate()
            }
            RunSortType.RUN_TIME -> {
                runsSortedByRunTime()
            }
            RunSortType.CALORIES_BURNED -> {
                runsSortedByCaloriesBurned()
            }
            RunSortType.AVG_SPEED -> {
                runsSortedByAvgSpeed()
            }
            RunSortType.DISTANCE -> {
                runsSortedByDistance()
            }
            RunSortType.STEP_COUNT -> {
                runsSortedByStepCount()
            }
        }
    }.also {
        this@RunsViewModel.runSortType = sortType
        savedStateHandle["runSortType"] = sortType
    }

    fun getAllRunsFromRemoteDatabase() = flow<List<Run>> {
        runUseCases.getAllRunsFromRemoteDBUseCase.invoke().collectLatest { remoteDBResponse ->
            when (remoteDBResponse) {
                is Resource.Error -> {
                    _runEventsChannel.emit(EventState.Error(remoteDBResponse.message.toString()))
                }
                is Resource.Loading -> {
                    _runEventsChannel.emit(EventState.Loading())
                }
                is Resource.Success -> {
                    _runEventsChannel.emit(EventState.Success())
                }
            }
        }
    }

}