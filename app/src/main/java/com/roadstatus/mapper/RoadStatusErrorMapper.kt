package com.roadstatus.mapper

import com.google.gson.Gson
import com.roadstatus.data.ErrorDTO
import com.roadstatus.view.model.RoadStatus
import javax.inject.Inject

class RoadStatusErrorMapper @Inject constructor() {
    fun getRoadStatusError(errorDTO: ErrorDTO): RoadStatus.Error {
        return RoadStatus.Error(errorDTO.message)
    }
}