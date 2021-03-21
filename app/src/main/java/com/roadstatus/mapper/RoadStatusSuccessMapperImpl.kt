package com.roadstatus.mapper

import com.roadstatus.data.StatusDTO
import com.roadstatus.view.model.BoundCoordinate
import com.roadstatus.view.model.RoadStatus
import javax.inject.Inject

class RoadStatusSuccessMapperImpl @Inject constructor() : RoadStatusSuccessMapper {
    override fun getRoadStatus(statusDTO: List<StatusDTO>): RoadStatus.Success {
        return statusDTO.take(1).map {
            val boundCoordinates = getBoundCoordinates(it)
            RoadStatus.Success(
                roadName = it.displayName,
                severity = it.statusSeverity,
                severityDescription = it.statusSeverityDescription,
                boundCoordinates = boundCoordinates
            )
        }.first()
    }

    private fun getBoundCoordinates(statusDTO: StatusDTO): MutableList<BoundCoordinate> {
        val boundCoordinates = mutableListOf<BoundCoordinate>()
        if (statusDTO.bounds.isNotEmpty()) {
            parseBoundCoordinates(statusDTO, boundCoordinates)
        }
        return boundCoordinates
    }

    private fun parseBoundCoordinates(statusDTO: StatusDTO, boundCoordinates: MutableList<BoundCoordinate>) {
        statusDTO.bounds.removePrefix("[[").removeSuffix("]]").split("],[").toTypedArray().map { bound ->
            val latLong = bound.split(",")
            boundCoordinates.add(BoundCoordinate(latLong.first(), latLong.last()))
        }
    }
}