package com.roadstatus.mapper

import com.roadstatus.data.ErrorDTO
import com.roadstatus.view.model.RoadStatus

interface RoadStatusErrorMapper {
    fun getRoadStatusError(errorDTO: ErrorDTO): RoadStatus.Error
}