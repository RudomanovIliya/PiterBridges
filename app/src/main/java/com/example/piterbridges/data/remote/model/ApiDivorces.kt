package com.example.piterbridges.data.remote.model

import com.example.piterbridges.presentation.bridges.model.Divorces
import com.google.gson.annotations.SerializedName

class ApiDivorces(
    @SerializedName("start") val startTime: String?,
    @SerializedName("end") val endTime: String?
)

fun ApiDivorces.toModel(): Divorces {
    return Divorces(
        startTime = startTime.orEmpty(),
        endTime = endTime.orEmpty()
    )
}