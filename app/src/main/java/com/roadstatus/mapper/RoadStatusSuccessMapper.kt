package com.roadstatus.mapper

import com.roadstatus.data.StatusDTO
import com.roadstatus.view.model.RoadStatus

interface RoadStatusSuccessMapper {
    fun getRoadStatus(statusDTO: List<StatusDTO>): RoadStatus.Success
}