package com.roadstatus.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roadstatus.repository.RoadStatusRepository
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

    private val viewStateEmitter = MutableLiveData<ViewState>(ViewState.Loading(isInProgress = false, isButtonEnabled = false))
    val viewState: LiveData<ViewState> = viewStateEmitter

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewStateEmitter.postValue(
            ViewState.Error(
                errorDescription = exception.message ?: "Something went Wrong!!",
                isInProgress = false,
                isButtonEnabled = true
            )
        )
    }

    fun onFetchButtonClicked(roadName: String) {
        viewStateEmitter.postValue(ViewState.Loading(isInProgress = true, isButtonEnabled = false))
        viewModelScope.launch {
            val updatedViewState = withContext(dispatcher.io() + coroutineExceptionHandler) {
                when (val roadStatus = roadStatusRepository.getRoadStatus(roadName)) {
                    is RoadStatus.Success -> {
                        ViewState.Content(
                            roadStatus = roadStatus,
                            isInProgress = false,
                            isButtonEnabled = true
                        )
                    }
                    is RoadStatus.Error ->
                        ViewState.Error(
                            errorDescription = roadStatus.reason,
                            isInProgress = false,
                            isButtonEnabled = true
                        )
                }
            }
            viewStateEmitter.postValue(updatedViewState)
        }
    }

    fun onTextChanged(input: String) {
        val newLoadingState =
            if (input.isNotEmpty())
                ViewState.Loading(isInProgress = false, isButtonEnabled = true)
            else
                ViewState.Loading(isInProgress = false, isButtonEnabled = false)
        viewStateEmitter.postValue(newLoadingState)
    }

    sealed class ViewState {
        abstract val isInProgress: Boolean
        abstract val isButtonEnabled: Boolean

        data class Loading(override val isInProgress: Boolean, override val isButtonEnabled: Boolean) : ViewState()
        data class Content(val roadStatus: RoadStatus.Success, override val isInProgress: Boolean, override val isButtonEnabled: Boolean) : ViewState()
        data class Error(val errorDescription: String, override val isInProgress: Boolean, override val isButtonEnabled: Boolean) : ViewState()
    }
}