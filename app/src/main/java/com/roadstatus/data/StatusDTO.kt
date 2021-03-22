package com.roadstatus.data

import com.google.gson.annotations.SerializedName

data class StatusDTO(
    @SerializedName("id") val id: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("statusSeverity") val statusSeverity: String, // Preferred to be a ENUM
    @SerializedName("statusSeverityDescription") val statusSeverityDescription: String,
    @SerializedName("bounds") val bounds: String,
    @SerializedName("envelope") val envelope: String,
    @SerializedName("url") val url: String
)
