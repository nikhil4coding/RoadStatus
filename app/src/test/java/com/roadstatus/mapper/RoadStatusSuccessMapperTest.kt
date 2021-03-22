package com.roadstatus.mapper

import com.google.common.truth.Truth.assertThat
import com.roadstatus.data.StatusDTO
import com.roadstatus.view.model.BoundCoordinate
import org.junit.Test

internal class RoadStatusSuccessMapperTest {
    private val sut = RoadStatusSuccessMapper()

    @Test
    fun `GIVEN single item in list THEN verify return value has correct values`() {
        val statusDTO = StatusDTO(
            id = "",
            displayName = "ROAD_NAME",
            statusSeverity = "SEVERITY",
            statusSeverityDescription = "SEVERITY_DESCRIPTION",
            bounds = "",
            envelope = "",
            url = ""
        )

        val result = sut.map(listOf(statusDTO))

        assertThat(result.roadName).isEqualTo("ROAD_NAME")
        assertThat(result.severity).isEqualTo("SEVERITY")
        assertThat(result.severityDescription).isEqualTo("SEVERITY_DESCRIPTION")
        assertThat(result.boundCoordinates).isEqualTo(listOf<BoundCoordinate>())
    }

    @Test
    fun `GIVEN multiple items in the list THEN verify return value has correct values`() {
        val statusDTO1 = StatusDTO(
            id = "",
            displayName = "ROAD_NAME1",
            statusSeverity = "SEVERITY1",
            statusSeverityDescription = "SEVERITY_DESCRIPTION1",
            bounds = "",
            envelope = "",
            url = ""
        )
        val statusDTO2 = StatusDTO(
            id = "",
            displayName = "ROAD_NAME2",
            statusSeverity = "SEVERITY2",
            statusSeverityDescription = "SEVERITY_DESCRIPTION2",
            bounds = "",
            envelope = "",
            url = ""
        )

        val result = sut.map(listOf(statusDTO1, statusDTO2))

        assertThat(result.roadName).isEqualTo("ROAD_NAME1")
        assertThat(result.severity).isEqualTo("SEVERITY1")
        assertThat(result.severityDescription).isEqualTo("SEVERITY_DESCRIPTION1")
        assertThat(result.boundCoordinates).isEqualTo(listOf<BoundCoordinate>())
    }
}