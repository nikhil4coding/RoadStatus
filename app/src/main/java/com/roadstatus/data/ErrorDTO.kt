package com.roadstatus.data

import com.google.gson.annotations.SerializedName

data class ErrorDTO(
    @SerializedName("timestampUTC") val timestampUTC: String,
    @SerializedName("exceptionType") val exceptionType: String,
    @SerializedName("httpStatusCode") val httpStatusCode: Long,
    @SerializedName("httpStatus") val httpStatus: String,
    @SerializedName("relativeURI") val relativeURI: String,
    @SerializedName("message") val message: String
)
