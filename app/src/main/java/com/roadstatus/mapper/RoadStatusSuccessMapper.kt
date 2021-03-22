package com.roadstatus.mapper

import com.roadstatus.data.StatusDTO
import com.roadstatus.view.model.BoundCoordinate
import com.roadstatus.view.model.RoadStatus
import javax.inject.Inject

class RoadStatusSuccessMapper @Inject constructor() {
    fun map(statusDTO: List<StatusDTO>): RoadStatus.Success {
        val status = statusDTO.first()
        val boundCoordinates = status.getBoundCoordinates()
        return RoadStatus.Success(
            roadName = status.displayName,
            severity = status.statusSeverity,
            severityDescription = status.statusSeverityDescription,
            boundCoordinates = boundCoordinates
        )
    }

    private fun StatusDTO.getBoundCoordinates(): List<BoundCoordinate> {
        val boundCoordinates = mutableListOf<BoundCoordinate>()
        if (this.bounds.isNotEmpty()) {
            parseBoundCoordinates(this, boundCoordinates)
        }
        return boundCoordinates
    }

    private fun parseBoundCoordinates(statusDTO: StatusDTO, boundCoordinates: MutableList<BoundCoordinate>) {
        statusDTO.bounds.removePrefix("[[").removeSuffix("]]").split("],[").map { bound ->
            val latLong = bound.split(",")
            boundCoordinates.add(BoundCoordinate(latLong.first().toDouble(), latLong.last().toDouble()))
        }
    }
}