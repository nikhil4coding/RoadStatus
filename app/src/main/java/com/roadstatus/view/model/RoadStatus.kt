package com.roadstatus.view.model

sealed class RoadStatus {
    data class Success(
        val roadName: String,
        val severity: String,
        val severityDescription: String,
        val boundCoordinates: List<BoundCoordinate>
    ): RoadStatus()

    data class Error(
        val reason: String
    ): RoadStatus()
}
