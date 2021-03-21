package com.roadstatus.repository

import com.google.gson.Gson
import com.roadstatus.data.StatusDTO
import com.roadstatus.mapper.RoadStatusErrorMapper
import com.roadstatus.mapper.RoadStatusSuccessMapper
import com.roadstatus.network.RoadStatusService
import com.roadstatus.view.model.RoadStatus
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

private const val ROAD_NAME = "ROAD_NAME"

internal class RoadStatusRepositoryTest {
    private val roadStatusService = mockk<RoadStatusService>()
    private val roadStatusSuccessMapper = mockk<RoadStatusSuccessMapper>()
    private val roadStatusErrorMapper = mockk<RoadStatusErrorMapper>()
    private val gson = mockk<Gson>()

    private val sut = RoadStatusRepositoryImpl(
        roadStatusService = roadStatusService,
        roadStatusSuccessMapper = roadStatusSuccessMapper,
        roadStatusErrorMapper = roadStatusErrorMapper,
        gson = gson
    )

    @Test
    fun `GIVEN name of the road AND if response is successful with non empty list THEN return Success`() = runBlocking {
        val response = getSuccessfulResponse()
        coEvery { roadStatusService.getRoadStatus(ROAD_NAME) } returns response

        val success = RoadStatus.Success(
            roadName = "ROAD_NAME",
            severity = "SEVERITY",
            severityDescription = "SEVERITY_DESCRIPTION",
            boundCoordinates = listOf()
        )
        every { roadStatusSuccessMapper.getRoadStatus(any()) } returns success

        val roadStatus = sut.getRoadStatus(ROAD_NAME)

        assert((roadStatus as RoadStatus.Success).roadName == "ROAD_NAME")
        assert((roadStatus).severity == "SEVERITY")
        assert((roadStatus).severityDescription == "SEVERITY_DESCRIPTION")
    }

    @Test
    fun `GIVEN name of the road AND if response is successful but null THEN return Error `() = runBlocking {
        coEvery { roadStatusService.getRoadStatus(ROAD_NAME) } returns Response.success(null)

        val roadStatus = sut.getRoadStatus(ROAD_NAME)

        assert((roadStatus as RoadStatus.Error).reason == "No Status received for valid Road Name")
    }

    private fun getSuccessfulResponse(): Response<List<StatusDTO>> {
        val statusDTO = StatusDTO(
            id = "",
            displayName = "ROAD_NAME",
            statusSeverity = "SEVERITY",
            statusSeverityDescription = "SEVERITY_DESCRIPTION",
            bounds = "",
            envelope = "",
            url = ""
        )
        return Response.success(listOf(statusDTO))
    }
}