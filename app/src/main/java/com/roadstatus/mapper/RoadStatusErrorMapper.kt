package com.roadstatus.mapper

import com.roadstatus.data.ErrorDTO
import com.roadstatus.view.model.RoadStatus
import javax.inject.Inject

class RoadStatusErrorMapper @Inject constructor() {
    fun map(errorDTO: ErrorDTO): RoadStatus.Error {
        return RoadStatus.Error(errorDTO.message)
    }
}