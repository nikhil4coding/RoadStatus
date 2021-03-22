package com.roadstatus.view

import androidx.lifecycle.Observer
import com.roadstatus.repository.RoadStatusRepository
import com.roadstatus.repository.RoadStatusRepositoryImpl
import com.roadstatus.utils.CoroutineTestRule
import com.roadstatus.utils.InstantExecutorExtension
import com.roadstatus.utils.observeWithMock
import com.roadstatus.view.model.RoadStatus
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

private const val ROAD_NAME = "ROAD_NAME"

@ExtendWith(InstantExecutorExtension::class)
@ExperimentalCoroutinesApi
internal class RoadStatusViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val coroutineTestRule = CoroutineTestRule()
    }

    private val roadStatusRepo: RoadStatusRepository = mockk<RoadStatusRepositoryImpl>()
    private lateinit var sut: RoadStatusViewModel
    private lateinit var viewStateObserver: Observer<RoadStatusViewModel.ViewState>

    @BeforeEach
    fun setup() {
        sut = RoadStatusViewModel(coroutineTestRule.testDispatcherProvider, roadStatusRepo)
        viewStateObserver = sut.viewState.observeWithMock()
    }

    @Test
    fun `GIVEN Initial state THEN Loading State is emitted`() {
        val expectedViewState = RoadStatusViewModel.ViewState.Loading(isInProgress = false, isButtonEnabled = false)
        verify { viewStateObserver.onChanged(expectedViewState) }
    }

    @Test
    fun `GIVEN a roadName WHEN Response is Success THEN Content State with actual data is emitted`() = coroutineTestRule.runBlockingTest {
        val roadStatus = RoadStatus.Success(
            roadName = ROAD_NAME,
            severity = "Good",
            severityDescription = "No Delays",
            boundCoordinates = listOf()
        )

        coEvery { roadStatusRepo.getRoadStatus(ROAD_NAME) } returns roadStatus

        sut.onFetchButtonClicked(ROAD_NAME)

        val loadingViewState = RoadStatusViewModel.ViewState.Loading(isInProgress = false, isButtonEnabled = false)
        val inProgressViewState = RoadStatusViewModel.ViewState.Loading(isInProgress = true, isButtonEnabled = false)
        val contentViewState = RoadStatusViewModel.ViewState.Content(
            roadStatus = roadStatus,
            isInProgress = false,
            isButtonEnabled = true
        )
        verifySequence {
            viewStateObserver.onChanged(loadingViewState)
            viewStateObserver.onChanged(inProgressViewState)
            viewStateObserver.onChanged(contentViewState)
        }
    }

    @Test
    fun `GIVEN a roadName WHEN Response is Error THEN Content State with Error message is emitted`() = coroutineTestRule.runBlockingTest {
        val roadStatus = RoadStatus.Error(
            reason = "Bad road name"
        )

        coEvery { roadStatusRepo.getRoadStatus(ROAD_NAME) } returns roadStatus

        sut.onFetchButtonClicked(ROAD_NAME)

        val loadingViewState = RoadStatusViewModel.ViewState.Loading(isInProgress = false, isButtonEnabled = false)
        val inProgressViewState = RoadStatusViewModel.ViewState.Loading(isInProgress = true, isButtonEnabled = false)
        val errorViewState = RoadStatusViewModel.ViewState.Error(
            errorDescription = "Bad road name",
            isInProgress = false,
            isButtonEnabled = true
        )
        verifySequence {
            viewStateObserver.onChanged(loadingViewState)
            viewStateObserver.onChanged(inProgressViewState)
            viewStateObserver.onChanged(errorViewState)
        }
    }
}