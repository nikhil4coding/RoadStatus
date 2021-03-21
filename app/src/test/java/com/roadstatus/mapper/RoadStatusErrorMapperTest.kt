package com.roadstatus.mapper

import com.roadstatus.data.ErrorDTO
import com.roadstatus.view.model.RoadStatus
import org.junit.Test

internal class RoadStatusErrorMapperTest {

    private val sut = RoadStatusErrorMapper()

    @Test
    fun `GIVEN Error DTO THEN verify that the return value has error message`() {
        val errorDTO = ErrorDTO(
            timestampUTC = "",
            exceptionType = "",
            httpStatusCode = 404,
            httpStatus = "",
            relativeURI = "",
            message = "ERROR"
        )
        val result = sut.getRoadStatusError(errorDTO)

        assert(result.reason == "ERROR")
    }
}