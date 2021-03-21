package com.roadstatus.mapper

import com.roadstatus.data.StatusDTO
import com.roadstatus.view.model.BoundCoordinate
import org.junit.Test

internal class RoadStatusSuccessMapperTest {
    private val sut = RoadStatusSuccessMapper()

    @Test
    fun `GIVEN StatusDTO THEN verify return value has correct values`() {

        val statusDTO = StatusDTO(
            id = "",
            displayName = "ROAD_NAME",
            statusSeverity = "SEVERITY",
            statusSeverityDescription = "SEVERITY_DESCRIPTION",
            bounds = "",
            envelope = "",
            url = ""
        )

        val result = sut.getRoadStatus(listOf(statusDTO))

        assert(result.roadName == "ROAD_NAME")
        assert(result.severity == "SEVERITY")
        assert(result.severityDescription == "SEVERITY_DESCRIPTION")
        assert(result.boundCoordinates == listOf<BoundCoordinate>())
    }
}