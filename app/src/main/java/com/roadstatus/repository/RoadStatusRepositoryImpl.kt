package com.roadstatus.repository

import com.google.gson.Gson
import com.roadstatus.data.ErrorDTO
import com.roadstatus.mapper.RoadStatusErrorMapper
import com.roadstatus.mapper.RoadStatusSuccessMapper
import com.roadstatus.network.RoadStatusService
import com.roadstatus.view.model.RoadStatus
import javax.inject.Inject

class RoadStatusRepositoryImpl @Inject constructor(
    private val roadStatusService: RoadStatusService,
    private val roadStatusSuccessMapper: RoadStatusSuccessMapper,
    private val roadStatusErrorMapper: RoadStatusErrorMapper,
    private val gson: Gson
) : RoadStatusRepository {
    override suspend fun getRoadStatus(name: String): RoadStatus {

        val response = roadStatusService.getRoadStatus(name)
        return if (response.isSuccessful) {
            val statusList = response.body()
            statusList?.let {
                roadStatusSuccessMapper.getRoadStatus(it)
            } ?: RoadStatus.Error("No Status received for valid Road Name")
        } else {
            response.errorBody()?.let {
                val errorDTO = gson.fromJson(it.string(), ErrorDTO::class.java)
                roadStatusErrorMapper.getRoadStatusError(errorDTO)
            } ?: RoadStatus.Error("No Error Status received for invalid Road Name")
        }
    }
}