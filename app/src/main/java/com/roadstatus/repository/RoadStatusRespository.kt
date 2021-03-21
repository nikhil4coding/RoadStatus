package com.roadstatus.repository

import com.roadstatus.view.model.RoadStatus

interface RoadStatusRepository {
    suspend fun getRoadStatus(roadName: String): RoadStatus
}