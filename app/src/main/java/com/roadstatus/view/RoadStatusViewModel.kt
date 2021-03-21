package com.roadstatus.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roadstatus.repository.RoadStatusRepository
import com.roadstatus.repository.RoadStatusRepositoryImpl
import com.roadstatus.utils.DispatcherProvider
import com.roadstatus.view.model.RoadStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RoadStatusViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val roadStatusRepository: RoadStatusRepository
) : ViewModel() {

    private val viewStateEmitter = MutableLiveData<ViewState>()
    private val viewEventsEmitter = MutableLiveData<ViewEvent>(ViewEvent.InProgress(false))
    val viewState: LiveData<ViewState> = viewStateEmitter

    val viewEvent: LiveData<ViewEvent> = viewEventsEmitter
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewStateEmitter.postValue(ViewState.Content.Error(exception.message ?: "Something went Wrong!!"))
    }

    fun onFetchButtonClicked(roadName: String) {
        viewEventsEmitter.postValue(ViewEvent.InProgress(true))
        viewModelScope.launch {
            withContext(dispatcher.io() + coroutineExceptionHandler) {
                when (val roadStatus = roadStatusRepository.getRoadStatus(roadName)) {
                    is RoadStatus.Success -> {
                        viewStateEmitter.postValue(
                            ViewState.Content.Success(
                                roadStatus = roadStatus
                            )
                        )
                    }
                    is RoadStatus.Error -> viewStateEmitter.postValue(
                        ViewState.Content.Error(
                            errorDescription = roadStatus.reason
                        )
                    )
                }
                viewEventsEmitter.postValue(ViewEvent.InProgress(false))
            }
        }
    }

    sealed class ViewState {
        sealed class Content : ViewState() {
            data class Success(
                val roadStatus: RoadStatus.Success,
            ) : Content()

            data class Error(
                val errorDescription: String,
            ) : Content()
        }
    }

    sealed class ViewEvent {
        data class InProgress(val showProgress: Boolean) : ViewEvent()
    }
}