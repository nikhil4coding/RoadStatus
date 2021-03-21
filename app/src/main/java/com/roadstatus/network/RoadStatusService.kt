package com.roadstatus.network

import com.roadstatus.BuildConfig
import com.roadstatus.data.StatusDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RoadStatusService {

    @GET("/Road/{roadName}?app_key=" + BuildConfig.APP_KEY)
    suspend fun getRoadStatus(@Path("roadName") name: String): Response<List<StatusDTO>>
}