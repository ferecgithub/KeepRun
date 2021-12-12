package com.ferechamitbeyli.presentation.viewmodel.activities.home_activity.fragments

import com.ferechamitbeyli.domain.Resource
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.presentation.uimodels.RunUIModel
import com.ferechamitbeyli.presentation.uimodels.mappers.RunToUIMapper
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.fromBitmap
import com.ferechamitbeyli.presentation.utils.states.EventState
import com.ferechamitbeyli.presentation.utils.usecases.RunUseCases
import com.ferechamitbeyli.presentation.utils.usecases.SessionUseCases
import com.ferechamitbeyli.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val sessionUseCases: SessionUseCases,
    private val runUseCases: RunUseCases,
    networkConnectionTracker: NetworkConnectionTracker,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(networkConnectionTracker, coroutineDispatchers) {

    private var _trackingEventsChannel = MutableSharedFlow<EventState>()
    val trackingEventsChannel: SharedFlow<EventState> = _trackingEventsChannel

    fun getUserWeightFromCache() = flow<Double> {
        sessionUseCases.getUserWeightUseCase.invoke().collect {
            emit(it)
        }
    }

    fun saveRunToDatabase(run: RunUIModel) = ioScope.launch {
        val runToBeSaved = RunToUIMapper.mapToDomainModel(run)
        runUseCases.insertRunUseCase.invoke(runToBeSaved)

    }

    fun saveRunToRemoteDatabase(run: RunUIModel) = ioScope.launch {
        val runToBeSaved = RunToUIMapper.mapToDomainModel(run)
        if (run.image != null) {
            runUseCases.insertMapImageToRemoteDBUseCase.invoke(
                fromBitmap(run.image),
                run.timestamp.toString()
            ).collect { imageUrlResponse ->
                runUseCases.insertRunToRemoteDBUseCase.invoke(
                    runToBeSaved,
                    imageUrlResponse.data.toString()
                ).collect { databaseResponse ->
                    when(databaseResponse) {
                        is Resource.Error -> {
                            _trackingEventsChannel.emit(EventState.Error(databaseResponse.message.toString()))
                        }
                        is Resource.Loading -> {
                            /** NO-OP **/
                        }
                        is Resource.Success -> {
                            _trackingEventsChannel.emit(EventState.Success(databaseResponse.data))
                        }
                    }
                }
            }
        } else {
            runUseCases.insertRunToRemoteDBUseCase.invoke(runToBeSaved, "")
        }
    }


}