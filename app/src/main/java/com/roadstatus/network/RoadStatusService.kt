package com.roadstatus.network

import com.roadstatus.BuildConfig
import com.roadstatus.data.StatusDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RoadStatusService {

    @GET("/Road/{roadName}")
    suspend fun getRoadStatus(
        @Path("roadName") name: String,
        @Query("app_key") appKey: String = BuildConfig.APP_KEY
    ): Response<List<StatusDTO>>
}