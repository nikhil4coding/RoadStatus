package com.roadstatus.mapper

import com.google.common.truth.Truth.assertThat
import com.roadstatus.data.ErrorDTO
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
        val result = sut.map(errorDTO)

        assertThat(result.reason).isEqualTo("ERROR")
    }
}