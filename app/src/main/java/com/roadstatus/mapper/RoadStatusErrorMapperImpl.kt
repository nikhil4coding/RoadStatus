package com.roadstatus.mapper

import com.roadstatus.data.ErrorDTO
import com.roadstatus.view.model.RoadStatus
import javax.inject.Inject

class RoadStatusErrorMapperImpl @Inject constructor() : RoadStatusErrorMapper {
    override fun getRoadStatusError(errorDTO: ErrorDTO): RoadStatus.Error {
        return RoadStatus.Error(errorDTO.message)
    }
}